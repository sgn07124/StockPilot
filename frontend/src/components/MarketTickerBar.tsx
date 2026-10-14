import React from 'react'
import { TrendingUp, TrendingDown, RefreshCw } from 'lucide-react'
import { MOCK_TICKERS } from '../services/mockData'

export const MarketTickerBar: React.FC = () => {
  return (
    <div className="h-8 bg-[#0b0e14] border-b border-[#1c2331] px-4 flex items-center justify-between text-xs overflow-x-auto select-none no-scrollbar">
      {/* Left Market Tickers List */}
      <div className="flex items-center space-x-6 shrink-0">
        <div className="flex items-center space-x-1.5 text-slate-500 font-semibold text-[11px] uppercase tracking-wider pr-2 border-r border-[#1e2636]">
          <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse"></span>
          <span>KR Market</span>
        </div>

        {MOCK_TICKERS.map((ticker) => {
          const isUp = ticker.changePct > 0
          const isDown = ticker.changePct < 0

          return (
            <div key={ticker.id} className="flex items-center space-x-2 shrink-0 cursor-default">
              <span className="text-slate-400 font-medium">{ticker.name}</span>
              <span className="font-semibold text-slate-100">{ticker.value}</span>
              <span
                className={`flex items-center text-[11px] font-semibold ${
                  isUp
                    ? 'text-red-500' // 국내 표준 상승 빨강
                    : isDown
                    ? 'text-blue-400' // 국내 표준 하락 파랑
                    : 'text-slate-400'
                }`}
              >
                {isUp && <TrendingUp size={11} className="mr-0.5" />}
                {isDown && <TrendingDown size={11} className="mr-0.5" />}
                {ticker.change} ({isUp ? '+' : ''}
                {ticker.changePct}%)
              </span>
            </div>
          )
        })}
      </div>

      {/* Right Last Sync Indicator */}
      <div className="hidden lg:flex items-center space-x-2 text-[11px] text-slate-500 shrink-0 pl-4">
        <RefreshCw size={11} className="text-slate-500 hover:text-slate-300 cursor-pointer transition-colors" />
        <span>정규장 마감 동기화 (15:30)</span>
      </div>
    </div>
  )
}
