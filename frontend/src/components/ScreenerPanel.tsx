import { Filter, CheckCircle2 } from 'lucide-react'

import { useStockStore } from '../store/useStockStore'
import { MOCK_SCREENER_ITEMS } from '../services/mockData'

// 스크리너 카테고리 정의
const SCREENER_CATEGORIES = [
  {
    group: '국장 퀀트 스크린',
    items: [
      { id: 'desk_promising', name: 'Desk 유망주 (AI 멀티팩터)', count: 122 },
      { id: 'breakout', name: '성장주 피벗 돌파', count: 9 },
      { id: 'flow_accumulation', name: '외인/기관 수급 쌍끌이', count: 26 },
      { id: 'earnings_surprise', name: '실적 서프라이즈 퀄리티', count: 162 },
      { id: 'volume_surge', name: '거래량 폭발 (50D 대비 300%+)', count: 98 },
      { id: 'risk_warning', name: 'DART/기술적 리스크 경고', count: 289 },
    ],
  },
  {
    group: '투자 대가 스크린 (CAN SLIM)',
    items: [
      { id: 'oneil', name: "William O'Neil (CAN SLIM)", count: 34 },
      { id: 'buffett', name: 'Warren Buffett (해자/고ROE)', count: 18 },
      { id: 'graham', name: 'Benjamin Graham (안전마진 저평가)', count: 42 },
      { id: 'lynch', name: 'Peter Lynch (GARP 고성장)', count: 29 },
    ],
  },
]

export const ScreenerPanel: React.FC = () => {
  const { selectedSymbol, setSelectedSymbol, screenerCategory, setScreenerCategory, scoreTier, setScoreTier } =
    useStockStore()

  // 스코어 필터링 적용
  const filteredItems = MOCK_SCREENER_ITEMS.filter((item) => {
    if (scoreTier === 'S') return item.composite >= 85
    if (scoreTier === 'A+') return item.composite >= 75
    if (scoreTier === 'B+') return item.composite >= 65
    return true
  })

  return (
    <div className="h-[280px] bg-[#0c1017] border-t border-[#1c2331] flex flex-col select-none">
      {/* Screener Header Bar */}
      <div className="h-8 bg-[#111622] border-b border-[#1c2331] px-3 flex items-center justify-between text-xs">
        <div className="flex items-center space-x-3">
          <div className="flex items-center space-x-1.5 font-bold text-slate-200">
            <Filter size={13} className="text-blue-400" />
            <span>퀀트 스크리너 워크벤치</span>
          </div>
          <span className="text-[11px] text-slate-500 font-mono">
            {filteredItems.length} items · 정규장 마감 기준
          </span>
        </div>

        {/* Score Tier Filter Pills */}
        <div className="flex items-center space-x-1">
          <span className="text-[11px] text-slate-400 mr-1.5 font-medium">등급 필터:</span>
          {(['ALL', 'B+', 'A+', 'S'] as const).map((tier) => (
            <button
              key={tier}
              onClick={() => setScoreTier(tier)}
              className={`px-2 py-0.5 rounded text-[10px] font-semibold transition-all ${
                scoreTier === tier
                  ? 'bg-blue-600 text-white shadow-sm'
                  : 'bg-[#18202d] text-slate-400 hover:text-slate-200 border border-[#222b3a]'
              }`}
            >
              {tier === 'ALL' ? '전체' : tier}
            </button>
          ))}
        </div>
      </div>

      {/* Split Body: Left Categories + Right High-Density Table */}
      <div className="flex-1 flex overflow-hidden">
        {/* Left Categories Tree (220px) */}
        <div className="w-[230px] shrink-0 bg-[#0e121a] border-r border-[#1c2331] overflow-y-auto p-2 text-xs space-y-3">
          {SCREENER_CATEGORIES.map((catGroup, idx) => (
            <div key={idx} className="space-y-1">
              <div className="text-[10px] uppercase font-bold text-slate-500 px-2 py-1 tracking-wider">
                {catGroup.group}
              </div>
              <div className="space-y-0.5">
                {catGroup.items.map((item) => (
                  <button
                    key={item.id}
                    onClick={() => setScreenerCategory(item.id)}
                    className={`w-full text-left px-2 py-1.5 rounded flex items-center justify-between transition-colors ${
                      screenerCategory === item.id
                        ? 'bg-blue-600/20 text-blue-300 font-semibold border border-blue-500/30'
                        : 'text-slate-400 hover:text-slate-200 hover:bg-[#151c27]'
                    }`}
                  >
                    <span className="truncate pr-1 text-[11px]">{item.name}</span>
                    <span className="text-[10px] font-mono px-1 py-0.2 rounded bg-[#18202c] text-slate-500 shrink-0">
                      {item.count}
                    </span>
                  </button>
                ))}
              </div>
            </div>
          ))}
        </div>

        {/* Right High-Density Data Table */}
        <div className="flex-1 overflow-x-auto overflow-y-auto bg-[#0a0d13]">
          <table className="w-full text-left text-xs font-mono border-collapse whitespace-nowrap">
            <thead className="sticky top-0 bg-[#131824] text-[11px] text-slate-400 border-b border-[#1e2637] z-10">
              <tr>
                <th className="py-2 px-3 text-center w-8">#</th>
                <th className="py-2 px-3">종목코드</th>
                <th className="py-2 px-3">종목명</th>
                <th className="py-2 px-3 text-right">현재가</th>
                <th className="py-2 px-3 text-right">등락률</th>
                <th className="py-2 px-3 text-right">고점대비(%)</th>
                <th className="py-2 px-3 text-right">거래량(주)</th>
                <th className="py-2 px-3 text-right">50D대비(%)</th>
                <th className="py-2 px-3 text-right">시가총액(억)</th>
                <th className="py-2 px-3 text-center">Comp</th>
                <th className="py-2 px-3 text-center">RS</th>
                <th className="py-2 px-3 text-center">A/D</th>
                <th className="py-2 px-3 text-center">SMR</th>
                <th className="py-2 px-3">소속 산업</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#171e2c]">
              {filteredItems.map((item, index) => {
                const isSelected = item.symbol === selectedSymbol
                const isUp = item.changeRate > 0

                return (
                  <tr
                    key={item.symbol}
                    onClick={() => setSelectedSymbol(item.symbol)}
                    className={`cursor-pointer transition-colors ${
                      isSelected
                        ? 'bg-blue-600/20 text-white font-medium'
                        : 'hover:bg-[#141b26] text-slate-300'
                    }`}
                  >
                    <td className="py-1.5 px-3 text-center text-slate-500 text-[10px]">{index + 1}</td>
                    <td className="py-1.5 px-3 text-slate-400 font-medium">{item.symbol}</td>
                    <td className="py-1.5 px-3 font-sans font-semibold text-slate-100 flex items-center space-x-1.5">
                      <span>{item.name}</span>
                      {isSelected && <CheckCircle2 size={12} className="text-blue-400" />}
                    </td>
                    <td className="py-1.5 px-3 text-right font-semibold">
                      {item.currentPrice.toLocaleString()}원
                    </td>
                    <td
                      className={`py-1.5 px-3 text-right font-bold ${
                        isUp ? 'text-red-500' : 'text-blue-400'
                      }`}
                    >
                      {isUp ? '+' : ''}
                      {item.changeRate}%
                    </td>
                    <td className="py-1.5 px-3 text-right text-slate-400">{item.offHigh52w}%</td>
                    <td className="py-1.5 px-3 text-right text-slate-300">
                      {item.volume.toLocaleString()}
                    </td>
                    <td className="py-1.5 px-3 text-right font-semibold text-emerald-400">
                      +{item.volVs50d}%
                    </td>
                    <td className="py-1.5 px-3 text-right text-slate-300">
                      {item.marketCap.toLocaleString()}억
                    </td>
                    <td className="py-1.5 px-3 text-center font-bold text-purple-400">{item.composite}</td>
                    <td className="py-1.5 px-3 text-center font-bold text-blue-400">{item.rs}</td>
                    <td className="py-1.5 px-3 text-center text-emerald-400 font-bold">{item.adRating}</td>
                    <td className="py-1.5 px-3 text-center text-amber-400 font-bold">{item.smrRating}</td>
                    <td className="py-1.5 px-3 font-sans text-slate-400 text-[11px]">{item.industry}</td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
