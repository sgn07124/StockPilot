// 국내 거시경제 지표 및 마켓 지수
export interface MarketTicker {
  id: string;
  name: string;
  value: string;
  change: string;
  changePct: number; // 양수: 상승(Red), 음수: 하락(Blue)
  unit?: string;
}

// 활성 종목 요약 정보
export interface StockSummary {
  symbol: string;
  name: string;
  market: 'KOSPI' | 'KOSDAQ';
  sector: string;
  industry: string;
  currentPrice: number;
  changePrice: number;
  changeRate: number;
  changeRate5D?: number;
  volume: number;
  marketCap: number; // 억원 단위
  high52w: number;
  low52w: number;
  isBookmarked?: boolean;
}

// 4대 핵심 KPI 스코어 및 퀀트 레이팅
export interface StockRatings {
  deskComposite: number;      // 1~99 종합 퀀트 점수
  growthBreakout: number;     // 0~100 성장주 돌파 점수
  themeEdge: number;          // 0~100 테마 모멘텀 점수
  flowEdge: number;           // 0~100 외인/기관 수급 점수
  
  rsRating: number;           // 1~99 상대강도 백분위
  rs3Month: number;
  rs6Month: number;
  groupRs: number;            // 소속 산업군 평균 RS
  groupRank: number;          // 197개 산업군 중 순위
  
  adRating: 'A' | 'B' | 'C' | 'D' | 'E'; // 매집/분산
  smrRating: 'A' | 'B' | 'C' | 'D' | 'E'; // Sales-Margin-ROE
  dartRisk: 'Low' | 'Medium' | 'High';   // 공시 리스크
  
  per: number;
  pbr: number;
  roe: number;
  eps: number;
}

// Spring AI 분석 코멘트
export interface AiDiagnosis {
  trend: 'BULLISH' | 'BEARISH' | 'SIDEWAYS';
  summary: string;
  breakoutReason: string;
  flowReason: string;
  riskNotice: string;
  analyzedAt: string;
}

// 동종 산업군 Top RS 경쟁 종목
export interface GroupRsItem {
  symbol: string;
  name: string;
  rs: number;
  growth: number;
  composite: number;
  currentPrice: number;
  changeRate: number;
}

// 차트 캔들 및 오버레이 데이터
export interface ChartCandleData {
  time: string; // YYYY-MM-DD
  open: number;
  high: number;
  low: number;
  close: number;
  volume: number;
  ma10?: number;
  ma20?: number;
  ma50?: number;
  ma200?: number;
  rsLine?: number; // KOSPI 대비 상대강도 비율 곡선
  foreignNet?: number; // 외국인 누적 순매수
  institutionNet?: number; // 기관 누적 순매수
}

// 베이스 패턴 및 가격 레벨
export interface BasePatternInfo {
  weeks: number;
  lowDropPct: number;
  pivotPrice: number;
  buyUpperPrice: number; // Pivot + 5%
  riskPrice: number;     // 손절선
  targetPrice: number;   // 1차 목표가
}

// 하단 스크리너 종목 아이템
export interface ScreenerItem {
  symbol: string;
  name: string;
  currentPrice: number;
  changeRate: number;
  offHigh52w: number;
  volume: number;
  volVs50d: number;
  marketCap: number; // 억원
  composite: number;
  growth: number;
  rs: number;
  adRating: string;
  smrRating: string;
  industry: string;
}
