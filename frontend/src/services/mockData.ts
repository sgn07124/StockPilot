import type {
  MarketTicker,
  StockSummary,
  StockRatings,
  AiDiagnosis,
  GroupRsItem,
  ChartCandleData,
  BasePatternInfo,
  ScreenerItem,
} from '../types/stock'


// 1. 국내 거시경제 & 마켓 지수 티커
export const MOCK_TICKERS: MarketTicker[] = [
  { id: 'kospi', name: '코스피', value: '2,612.45', change: '+24.18', changePct: 0.93 },
  { id: 'kosdaq', name: '코스닥', value: '768.12', change: '-3.45', changePct: -0.45 },
  { id: 'usdkrw', name: '원/달러', value: '1,382.50', change: '-4.20', changePct: -0.30, unit: '원' },
  { id: 'ktb3y', name: '국고채 3년', value: '2.84%', change: '+0.02', changePct: 0.71 },
  { id: 'ktb10y', name: '국고채 10년', value: '2.96%', change: '+0.01', changePct: 0.34 },
  { id: 'callRate', name: '기준금리', value: '3.25%', change: '0.00', changePct: 0.0 },
]

// 2. 종목별 기본 및 퀀트 정보 맵
export const MOCK_STOCKS: Record<
  string,
  {
    summary: StockSummary;
    ratings: StockRatings;
    ai: AiDiagnosis;
    basePattern: BasePatternInfo;
    groupRs: GroupRsItem[];
  }
> = {
  '005930': {
    summary: {
      symbol: '005930',
      name: '삼성전자',
      market: 'KOSPI',
      sector: 'IT / 전기전자',
      industry: '반도체 및 반도체 장비',
      currentPrice: 72400,
      changePrice: 1600,
      changeRate: 2.26,
      changeRate5D: 3.42,
      volume: 14850230,
      marketCap: 4322120, // 432조원
      high52w: 88800,
      low52w: 58000,
      isBookmarked: true,
    },
    ratings: {
      deskComposite: 78,
      growthBreakout: 82,
      themeEdge: 74,
      flowEdge: 79,
      rsRating: 88,
      rs3Month: 91,
      rs6Month: 85,
      groupRs: 89,
      groupRank: 4,
      adRating: 'A',
      smrRating: 'A',
      dartRisk: 'Low',
      per: 14.8,
      pbr: 1.25,
      roe: 9.8,
      eps: 4890,
    },
    ai: {
      trend: 'BULLISH',
      summary: '60일선 저항을 거래량 동반 양봉으로 돌파하며 단기 정배열 진입 국면입니다. 외국인 수급이 5거래일 연속 순유입되고 있습니다.',
      breakoutReason: '10주간 형성된 베이스 상단 피벗 가격을 돌파 후 +2.2% 구간에서 거래량이 50일 평균 대비 185% 증가했습니다.',
      flowReason: '외인/기관 쌍끌이 순매수 유입 중이며, 전주 대비 기관 매집 강도가 A등급으로 상향되었습니다.',
      riskNotice: '직전 52주 고점 부근 매물대(75,000원) 진입 시 차익 실현 매물 출회 가능성에 유의하세요. 손절 기준가: 69,500원.',
      analyzedAt: '2026-10-14 15:30',
    },
    basePattern: {
      weeks: 10,
      lowDropPct: -14.2,
      pivotPrice: 71800,
      buyUpperPrice: 75390,
      riskPrice: 69500,
      targetPrice: 84000,
    },
    groupRs: [
      { symbol: '000660', name: 'SK하이닉스', rs: 96, growth: 95, composite: 89, currentPrice: 198000, changeRate: 3.67 },
      { symbol: '005930', name: '삼성전자', rs: 88, growth: 82, composite: 78, currentPrice: 72400, changeRate: 2.26 },
      { symbol: '042700', name: '한미반도체', rs: 86, growth: 91, composite: 76, currentPrice: 112000, changeRate: 1.82 },
      { symbol: '278280', name: '천보', rs: 72, growth: 55, composite: 61, currentPrice: 65400, changeRate: -0.61 },
    ],
  },
  '000660': {
    summary: {
      symbol: '000660',
      name: 'SK하이닉스',
      market: 'KOSPI',
      sector: 'IT / 전기전자',
      industry: '반도체 및 반도체 장비',
      currentPrice: 198000,
      changePrice: 7000,
      changeRate: 3.67,
      changeRate5D: 6.81,
      volume: 4890200,
      marketCap: 1441440,
      high52w: 204000,
      low52w: 112000,
      isBookmarked: true,
    },
    ratings: {
      deskComposite: 89,
      growthBreakout: 94,
      themeEdge: 92,
      flowEdge: 86,
      rsRating: 96,
      rs3Month: 97,
      rs6Month: 95,
      groupRs: 89,
      groupRank: 4,
      adRating: 'A',
      smrRating: 'A',
      dartRisk: 'Low',
      per: 11.2,
      pbr: 1.85,
      roe: 18.4,
      eps: 17680,
    },
    ai: {
      trend: 'BULLISH',
      summary: 'HBM 시장 주도력을 바탕으로 역대급 분기 영업이익 기대감이 반영되고 있으며, RS 96으로 시장 최상위 4% 이내의 주도주입니다.',
      breakoutReason: '신고가 영역에 근접하며 매물 공백 구간으로 진입, 20일선 지지 확인 후 전고점 트라이.',
      flowReason: '외국인의 지속적인 대량 누적 순매수로 A/D 레이팅 최우수 등급 유지 중.',
      riskNotice: '단기 RSI 과열(72) 구간이므로 피벗 추격 매수보다는 5일선 지지 테스트 시 분할 접근이 유리합니다.',
      analyzedAt: '2026-10-14 15:30',
    },
    basePattern: {
      weeks: 7,
      lowDropPct: -11.5,
      pivotPrice: 192000,
      buyUpperPrice: 201600,
      riskPrice: 185000,
      targetPrice: 225000,
    },
    groupRs: [
      { symbol: '000660', name: 'SK하이닉스', rs: 96, growth: 95, composite: 89, currentPrice: 198000, changeRate: 3.67 },
      { symbol: '005930', name: '삼성전자', rs: 88, growth: 82, composite: 78, currentPrice: 72400, changeRate: 2.26 },
      { symbol: '042700', name: '한미반도체', rs: 86, growth: 91, composite: 76, currentPrice: 112000, changeRate: 1.82 },
    ],
  },
  '161890': {
    summary: {
      symbol: '161890',
      name: '한국콜마',
      market: 'KOSPI',
      sector: '화장품 / 뷰티',
      industry: '화장품 제조업',
      currentPrice: 115800,
      changePrice: 9300,
      changeRate: 8.73,
      changeRate5D: 12.4,
      volume: 1240500,
      marketCap: 26400,
      high52w: 118000,
      low52w: 48500,
      isBookmarked: false,
    },
    ratings: {
      deskComposite: 85,
      growthBreakout: 91,
      themeEdge: 88,
      flowEdge: 84,
      rsRating: 93,
      rs3Month: 94,
      rs6Month: 92,
      groupRs: 86,
      groupRank: 8,
      adRating: 'A',
      smrRating: 'A',
      dartRisk: 'Low',
      per: 15.7,
      pbr: 2.63,
      roe: 14.69,
      eps: 7380,
    },
    ai: {
      trend: 'BULLISH',
      summary: 'K-뷰티 인디 브랜드 수출 급증에 따른 글로벌 ODM 수주 폭발로 주가와 실적이 동반 급등하는 강력한 모멘텀 구간입니다.',
      breakoutReason: '15주 컵위드핸들 베이스 피벗(108,000원)을 50일 이평 거래량 대비 320%의 대량 거래량과 함께 시원하게 돌파.',
      flowReason: '기관과 외국인의 8거래일 연속 쌍끌이 순매수 포착. 수급지수 84점 기록.',
      riskNotice: '피벗 상한선(+5%) 초과 영역이므로 눌림목 형성 시 지지선 확인 후 매수 추천.',
      analyzedAt: '2026-10-14 15:30',
    },
    basePattern: {
      weeks: 15,
      lowDropPct: -16.4,
      pivotPrice: 108000,
      buyUpperPrice: 113400,
      riskPrice: 102000,
      targetPrice: 135000,
    },
    groupRs: [
      { symbol: '161890', name: '한국콜마', rs: 93, growth: 91, composite: 85, currentPrice: 115800, changeRate: 8.73 },
      { symbol: '192820', name: '코스맥스', rs: 89, growth: 87, composite: 80, currentPrice: 174000, changeRate: 4.19 },
      { symbol: '090430', name: '아모레퍼시픽', rs: 76, growth: 68, composite: 69, currentPrice: 142000, changeRate: 1.43 },
    ],
  },
}

// 3. 차트 캔들 생성 헬퍼 (일봉 120일치 생성)
export function generateMockCandles(basePrice: number): ChartCandleData[] {
  const candles: ChartCandleData[] = []
  const today = new Date('2026-10-14')
  let currentClose = basePrice * 0.85 // 120일 전에는 약 15% 낮은 가격에서 시작
  let kospiIndex = 2500
  let foreignCumulative = 0
  let instCumulative = 0

  // 120거래일 역산하여 생성
  for (let i = 120; i >= 0; i--) {
    const d = new Date(today)
    d.setDate(d.getDate() - i * 1.4) // 주말 고려 대략적 날짜
    const dateStr = d.toISOString().split('T')[0]

    // 일별 변동률 시뮬레이션 (-2% ~ +2.5%)
    const dailyChangePct = (Math.random() - 0.47) * 0.04
    const open = Math.round(currentClose * (1 + (Math.random() - 0.5) * 0.01))
    const close = Math.round(open * (1 + dailyChangePct))
    const high = Math.round(Math.max(open, close) * (1 + Math.random() * 0.015))
    const low = Math.round(Math.min(open, close) * (1 - Math.random() * 0.015))
    const volume = Math.round(500000 + Math.random() * 2000000)

    currentClose = close
    kospiIndex = kospiIndex * (1 + (Math.random() - 0.49) * 0.012)
    const rsLine = parseFloat((close / kospiIndex * 100).toFixed(2))

    foreignCumulative += Math.round((Math.random() - 0.45) * 80000)
    instCumulative += Math.round((Math.random() - 0.46) * 60000)

    candles.push({
      time: dateStr,
      open,
      high,
      low,
      close,
      volume,
      rsLine,
      foreignNet: foreignCumulative,
      institutionNet: instCumulative,
    })
  }

  // 이동평균선 계산 (SMA 10, 20, 50, 200)
  for (let i = 0; i < candles.length; i++) {
    const calcMa = (period: number) => {
      if (i < period - 1) return undefined
      let sum = 0
      for (let j = 0; j < period; j++) {
        sum += candles[i - j].close
      }
      return Math.round(sum / period)
    }

    candles[i].ma10 = calcMa(10)
    candles[i].ma20 = calcMa(20)
    candles[i].ma50 = calcMa(50)
    candles[i].ma200 = calcMa(200)
  }

  return candles
}

// 4. 하단 스크리너 테이블 종목 목록
export const MOCK_SCREENER_ITEMS: ScreenerItem[] = [
  {
    symbol: '000660',
    name: 'SK하이닉스',
    currentPrice: 198000,
    changeRate: 3.67,
    offHigh52w: -2.9,
    volume: 4890200,
    volVs50d: 142.5,
    marketCap: 1441440,
    composite: 89,
    growth: 95,
    rs: 96,
    adRating: 'A',
    smrRating: 'A',
    industry: '반도체 및 반도체 장비',
  },
  {
    symbol: '161890',
    name: '한국콜마',
    currentPrice: 115800,
    changeRate: 8.73,
    offHigh52w: -1.8,
    volume: 1240500,
    volVs50d: 320.1,
    marketCap: 26400,
    composite: 85,
    growth: 91,
    rs: 93,
    adRating: 'A',
    smrRating: 'A',
    industry: '화장품 제조업',
  },
  {
    symbol: '005930',
    name: '삼성전자',
    currentPrice: 72400,
    changeRate: 2.26,
    offHigh52w: -18.4,
    volume: 14850230,
    volVs50d: 115.3,
    marketCap: 4322120,
    composite: 78,
    growth: 82,
    rs: 88,
    adRating: 'A',
    smrRating: 'A',
    industry: '반도체 및 반도체 장비',
  },
  {
    symbol: '035420',
    name: 'NAVER',
    currentPrice: 182500,
    changeRate: 1.39,
    offHigh52w: -24.2,
    volume: 890400,
    volVs50d: 98.2,
    marketCap: 298000,
    composite: 74,
    growth: 76,
    rs: 81,
    adRating: 'B',
    smrRating: 'B',
    industry: '인터넷 소프트웨어',
  },
  {
    symbol: '042700',
    name: '한미반도체',
    currentPrice: 112000,
    changeRate: 1.82,
    offHigh52w: -15.2,
    volume: 1120000,
    volVs50d: 135.0,
    marketCap: 108000,
    composite: 76,
    growth: 91,
    rs: 86,
    adRating: 'A',
    smrRating: 'A',
    industry: '반도체 장비',
  },
  {
    symbol: '192820',
    name: '코스맥스',
    currentPrice: 174000,
    changeRate: 4.19,
    offHigh52w: -4.5,
    volume: 450200,
    volVs50d: 180.4,
    marketCap: 19800,
    composite: 80,
    growth: 87,
    rs: 89,
    adRating: 'A',
    smrRating: 'A',
    industry: '화장품 제조업',
  },
  {
    symbol: '003680',
    name: '한성기업',
    currentPrice: 9300,
    changeRate: 9.93,
    offHigh52w: -15.4,
    volume: 3820100,
    volVs50d: 686.1,
    marketCap: 580,
    composite: 68,
    growth: 71,
    rs: 85,
    adRating: 'A',
    smrRating: 'B',
    industry: '식품 및 가공육',
  },
]
