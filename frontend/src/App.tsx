import React from 'react'
import { Gnb } from './components/Gnb'
import { MarketTickerBar } from './components/MarketTickerBar'
import { StockHeader } from './components/StockHeader'
import { LeftPanel } from './components/LeftPanel'
import { MainChart } from './components/MainChart'
import { ScreenerPanel } from './components/ScreenerPanel'

export const App: React.FC = () => {
  return (
    <div className="flex flex-col h-screen w-screen overflow-hidden bg-[#0b0e14] text-slate-100 font-sans">
      {/* 1. Global Navigation Bar */}
      <Gnb />

      {/* 2. 국내 거시경제 & 마켓 지수 티커 바 */}
      <MarketTickerBar />

      {/* 3. 활성 종목 헤더 & 4대 핵심 KPI 카드 */}
      <StockHeader />

      {/* 4. 중앙 3분할 워크벤치 메인 영역 */}
      <div className="flex-1 flex overflow-hidden">
        {/* 좌측 심층 분석 패널 (가로 350px) */}
        <LeftPanel />

        {/* 중앙 인터랙티브 차트 */}
        <MainChart />
      </div>

      {/* 5. 하단 퀀트 스크리너 & 고밀도 데이터 테이블 */}
      <ScreenerPanel />
    </div>
  )
}

export default App
