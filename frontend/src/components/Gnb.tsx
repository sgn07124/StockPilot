import React from 'react'
import { Search, Bell, Bookmark, User, Compass, TrendingUp, SlidersHorizontal } from 'lucide-react'
import { useStockStore } from '../store/useStockStore'

export const Gnb: React.FC = () => {
  const { setSelectedSymbol } = useStockStore()

  return (
    <header className="h-12 bg-[#0e121a] border-b border-[#1f2736] px-4 flex items-center justify-between select-none">
      {/* Brand & Left Menus */}
      <div className="flex items-center space-x-6">
        <div className="flex items-center space-x-2 cursor-pointer" onClick={() => setSelectedSymbol('005930')}>
          <div className="w-7 h-7 rounded-lg bg-gradient-to-tr from-blue-600 to-cyan-400 flex items-center justify-center font-black text-white text-sm shadow-md shadow-blue-500/20">
            SP
          </div>
          <span className="font-bold text-base tracking-tight text-white flex items-center">
            Stock<span className="text-blue-400">Pilot</span>
            <span className="ml-2 text-[10px] font-semibold tracking-wider px-1.5 py-0.5 rounded bg-blue-500/20 text-blue-400 border border-blue-500/30">
              DESK
            </span>
          </span>
        </div>

        <nav className="hidden md:flex items-center space-x-1 text-xs font-medium">
          <button className="px-3 py-1.5 rounded-md text-white bg-[#1b2230] font-semibold flex items-center space-x-1.5">
            <TrendingUp size={14} className="text-blue-400" />
            <span>Surge Desk</span>
          </button>
          <button className="px-3 py-1.5 rounded-md text-slate-400 hover:text-slate-200 hover:bg-[#151a24] transition-colors flex items-center space-x-1.5">
            <Compass size={14} />
            <span>마켓 맵</span>
          </button>
          <button className="px-3 py-1.5 rounded-md text-slate-400 hover:text-slate-200 hover:bg-[#151a24] transition-colors flex items-center space-x-1.5">
            <SlidersHorizontal size={14} />
            <span>퀀트 스크리너</span>
          </button>
        </nav>
      </div>

      {/* Center Search Input */}
      <div className="flex-1 max-w-md mx-6">
        <div className="relative flex items-center">
          <Search size={14} className="absolute left-3 text-slate-400" />
          <input
            type="text"
            placeholder="종목명 또는 6자리 코드 검색 (예: 삼성전자, 005930)... ⌘K"
            className="w-full bg-[#151b26] border border-[#232c3d] rounded-md pl-9 pr-10 py-1.5 text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-blue-500/80 focus:ring-1 focus:ring-blue-500/30 transition-all"
          />
          <kbd className="absolute right-2.5 text-[10px] font-mono bg-[#1f2736] text-slate-400 px-1.5 py-0.5 rounded border border-[#2d3748]">
            ⌘K
          </kbd>
        </div>
      </div>

      {/* Right User Actions */}
      <div className="flex items-center space-x-3 text-xs">
        <button
          onClick={() => setSelectedSymbol('000660')}
          className="text-slate-400 hover:text-white px-2 py-1 rounded hover:bg-[#18202d] transition-colors flex items-center space-x-1"
        >
          <Bookmark size={13} className="text-amber-400" />
          <span className="hidden sm:inline">관심종목</span>
        </button>

        <button className="p-1.5 rounded text-slate-400 hover:text-white hover:bg-[#18202d] transition-colors relative">
          <Bell size={15} />
          <span className="absolute top-1 right-1 w-1.5 h-1.5 bg-blue-500 rounded-full"></span>
        </button>

        <div className="h-4 w-[1px] bg-[#1f2736] mx-1"></div>

        <div className="flex items-center space-x-2 pl-1 cursor-pointer group">
          <div className="w-6 h-6 rounded-full bg-gradient-to-r from-purple-500 to-indigo-600 flex items-center justify-center text-white text-[11px] font-semibold">
            <User size={13} />
          </div>
          <span className="text-slate-300 font-medium group-hover:text-white transition-colors hidden sm:inline">
            Pilot User
          </span>
        </div>
      </div>
    </header>
  )
}
