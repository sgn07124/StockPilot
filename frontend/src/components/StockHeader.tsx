import React from 'react'
import { Star, ShieldAlert, Zap, Layers, Activity } from 'lucide-react'
import { useStockStore } from '../store/useStockStore'
import { MOCK_STOCKS } from '../services/mockData'

export const StockHeader: React.FC = () => {
  const { selectedSymbol, timeframe, setTimeframe } = useStockStore()
  const currentData = MOCK_STOCKS[selectedSymbol] || MOCK_STOCKS['005930']
  const { summary, ratings } = currentData

  const isUp = summary.changeRate > 0
  const isDown = summary.changeRate < 0

  return (
    <div className="bg-[#0f131c] border-b border-[#1c2331] px-4 py-3 select-none">
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-3">
        {/* Left: Stock Basic Info & Live Price */}
        <div className="flex items-start sm:items-center space-x-4">
          <div className="flex items-center space-x-2">
            <button className="text-slate-500 hover:text-amber-400 transition-colors p-1">
              <Star size={18} className={summary.isBookmarked ? 'fill-amber-400 text-amber-400' : ''} />
            </button>
            <div>
              <div className="flex items-center space-x-2">
                <h1 className="text-lg font-bold text-white tracking-tight">{summary.name}</h1>
                <span className="text-xs font-mono px-1.5 py-0.5 rounded bg-[#18202d] text-slate-400 border border-[#232c3d]">
                  {summary.symbol}
                </span>
                <span className="text-[11px] font-semibold text-blue-400 px-1.5 py-0.5 rounded bg-blue-500/10 border border-blue-500/20">
                  {summary.market}
                </span>
              </div>
              <p className="text-[11px] text-slate-400 mt-0.5">
                {summary.sector} · {summary.industry}
              </p>
            </div>
          </div>

          <div className="h-8 w-[1px] bg-[#1e2636] hidden sm:block"></div>

          {/* Price Numbers */}
          <div className="flex items-baseline space-x-3">
            <span className="text-2xl font-black tracking-tight text-white font-mono">
              {summary.currentPrice.toLocaleString()}원
            </span>
            <div className="flex items-center space-x-1.5 font-semibold text-xs font-mono">
              <span className={isUp ? 'text-red-500' : isDown ? 'text-blue-400' : 'text-slate-400'}>
                {isUp ? '▲' : isDown ? '▼' : '-'} {Math.abs(summary.changePrice).toLocaleString()}원 (
                {isUp ? '+' : ''}
                {summary.changeRate}%)
              </span>
              {summary.changeRate5D !== undefined && (
                <span className="text-[11px] text-slate-400 px-1.5 py-0.5 rounded bg-[#161c28]">
                  5D: {summary.changeRate5D > 0 ? '+' : ''}
                  {summary.changeRate5D}%
                </span>
              )}
            </div>
          </div>
        </div>

        {/* Right: 4 KPI Cards & Timeframe Selector */}
        <div className="flex items-center space-x-4">
          {/* Timeframe Pill Buttons */}
          <div className="hidden xl:flex items-center bg-[#151b26] p-0.5 rounded-lg border border-[#232d3d] text-xs font-semibold">
            {(['1D', '1W', '1M'] as const).map((tf) => (
              <button
                key={tf}
                onClick={() => setTimeframe(tf)}
                className={`px-3 py-1 rounded-md transition-all ${
                  timeframe === tf
                    ? 'bg-blue-600 text-white shadow-sm'
                    : 'text-slate-400 hover:text-slate-200'
                }`}
              >
                {tf === '1D' ? '일봉 (Daily)' : tf === '1W' ? '주봉 (Weekly)' : '월봉 (Monthly)'}
              </button>
            ))}
          </div>

          {/* 4대 KPI 스코어 요약 카드 그리드 */}
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-2">
            {/* Card 1: Desk Composite */}
            <div className="bg-[#141923] border border-[#232c3d] rounded-lg px-2.5 py-1.5 flex items-center space-x-2">
              <div className="w-7 h-7 rounded-md bg-purple-500/15 text-purple-400 flex items-center justify-center shrink-0">
                <Zap size={15} />
              </div>
              <div>
                <div className="text-[10px] text-slate-400 font-medium">COMPOSITE</div>
                <div className="text-sm font-bold font-mono text-purple-300">
                  {ratings.deskComposite}
                  <span className="text-[10px] text-slate-500 font-normal">/99</span>
                </div>
              </div>
            </div>

            {/* Card 2: 성장주 돌파 */}
            <div className="bg-[#141923] border border-[#232c3d] rounded-lg px-2.5 py-1.5 flex items-center space-x-2">
              <div className="w-7 h-7 rounded-md bg-blue-500/15 text-blue-400 flex items-center justify-center shrink-0">
                <Activity size={15} />
              </div>
              <div>
                <div className="text-[10px] text-slate-400 font-medium">성장주 돌파</div>
                <div className="text-sm font-bold font-mono text-blue-300">
                  {ratings.growthBreakout}
                  <span className="text-[10px] text-slate-500 font-normal">/100</span>
                </div>
              </div>
            </div>

            {/* Card 3: 테마 Edge */}
            <div className="bg-[#141923] border border-[#232c3d] rounded-lg px-2.5 py-1.5 flex items-center space-x-2">
              <div className="w-7 h-7 rounded-md bg-emerald-500/15 text-emerald-400 flex items-center justify-center shrink-0">
                <Layers size={15} />
              </div>
              <div>
                <div className="text-[10px] text-slate-400 font-medium">Theme Edge</div>
                <div className="text-sm font-bold font-mono text-emerald-300">
                  {ratings.themeEdge}
                  <span className="text-[10px] text-slate-500 font-normal">/100</span>
                </div>
              </div>
            </div>

            {/* Card 4: 수급 Flow Edge */}
            <div className="bg-[#141923] border border-[#232c3d] rounded-lg px-2.5 py-1.5 flex items-center space-x-2">
              <div className="w-7 h-7 rounded-md bg-amber-500/15 text-amber-400 flex items-center justify-center shrink-0">
                <ShieldAlert size={15} />
              </div>
              <div>
                <div className="text-[10px] text-slate-400 font-medium">Flow Edge</div>
                <div className="text-sm font-bold font-mono text-amber-300">
                  {ratings.flowEdge}
                  <span className="text-[10px] text-slate-500 font-normal">/100</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
