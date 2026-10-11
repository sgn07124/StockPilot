package com.stockpilot.common.exception;

import com.stockpilot.common.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    @RequestMapping("/test/api")
    static class TestController {

        @GetMapping("/success")
        public ApiResponse<String> success() {
            return ApiResponse.success("Hello StockPilot");
        }

        @GetMapping("/business-error")
        public ApiResponse<Void> businessError() {
            throw new StockNotFoundException("005930");
        }

        @GetMapping("/param-error")
        public ApiResponse<String> paramError(@RequestParam("name") String name) {
            return ApiResponse.success(name);
        }
    }

    @Test
    @DisplayName("성공 응답 시 success=true와 data를 반환한다")
    void successResponse() throws Exception {
        mockMvc.perform(get("/test/api/success")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Hello StockPilot"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("BusinessException 발생 시 지정된 HttpStatus와 에러 코드를 반환한다")
    void businessExceptionResponse() throws Exception {
        mockMvc.perform(get("/test/api/business-error")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.code").value("S001"))
                .andExpect(jsonPath("$.error.message").value("종목 코드를 찾을 수 없습니다: 005930"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("필수 파라미터 누락 시 400 Bad Request와 C002 에러를 반환한다")
    void missingParameterResponse() throws Exception {
        mockMvc.perform(get("/test/api/param-error")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("C002"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
