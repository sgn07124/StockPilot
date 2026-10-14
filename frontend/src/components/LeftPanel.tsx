import React from 'react'
import { Award, Sparkles, Trophy, BarChart3, AlertCircle } from 'lucide-react'
import { useStockStore } from '../store/useStockStore'
import { MOCK_STOCKS } from '../services/mockData'

export const LeftPanel: React.FC = () => {
  const { selectedSymbol, activeLeftTab, setActiveLeftTab, setSelectedSymbol } = useStockStore()
  const currentData = MOCK_STOCKS[selectedSymbol] || MOCK_STOCKS['005930']
  const { ratings, ai, groupRs } = currentData

  return (
    <aside className="w-full lg:w-[350px] shrink-0 bg-[#0e121a] border-r border-[#1c2331] flex flex-col h-full select-none overflow-hidden">
      {/* 4 Tabs Header */}
      <div className="flex border-b border-[#1c2331] bg-[#121622] text-xs font-semibold">
        <button
          onClick={() => setActiveLeftTab('ratings')}
          className={`flex-1 py-2.5 flex items-center justify-center space-x-1 border-b-2 transition-all ${
            activeLeftTab === 'ratings'
              ? 'border-blue-500 text-blue-400 bg-[#161c2b]'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <Award size={13} />
          <span>핵심 레이팅</span>
        </button>

        <button
          onClick={() => setActiveLeftTab('ai')}
          className={`flex-1 py-2.5 flex items-center justify-center space-x-1 border-b-2 transition-all ${
            activeLeftTab === 'ai'
              ? 'border-purple-500 text-purple-400 bg-[#161c2b]'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <Sparkles size={13} />
          <span>AI 진단</span>
        </button>

        <button
          onClick={() => setActiveLeftTab('groupRs')}
          className={`flex-1 py-2.5 flex items-center justify-center space-x-1 border-b-2 transition-all ${
            activeLeftTab === 'groupRs'
              ? 'border-emerald-500 text-emerald-400 bg-[#161c2b]'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <Trophy size={13} />
          <span>동종 RS</span>
        </button>

        <button
          onClick={() => setActiveLeftTab('financials')}
          className={`flex-1 py-2.5 flex items-center justify-center space-x-1 border-b-2 transition-all ${
            activeLeftTab === 'financials'
              ? 'border-amber-500 text-amber-400 bg-[#161c2b]'
              : 'border-transparent text-slate-400 hover:text-slate-200'
          }`}
        >
          <BarChart3 size={13} />
          <span>재무</span>
        </button>
      </div>

      {/* Tab Content Area */}
      <div className="flex-1 overflow-y-auto p-3 text-xs space-y-3">
        {/* TAB 1: 핵심 레이팅 (Grid View) */}
        {activeLeftTab === 'ratings' && (
          <div className="space-y-3">
            <div className="grid grid-cols-2 gap-2">
              <div className="bg-[#141924] p-2.5 rounded border border-[#20293a]">
                <div className="text-[11px] text-slate-400">RS 상대강도 (1~99)</div>
                <div className="text-base font-bold font-mono text-blue-400 mt-0.5">
                  {ratings.rsRating}{' '}
                  <span className="text-[10px] text-slate-400 font-normal">
                    {ratings.rsRating >= 90 ? '★주도주' : '양호'}
                  </span>
                </div>
              </div>

              <div className="bg-[#141924] p-2.5 rounded border border-[#20293a]">
                <div className="text-[11px] text-slate-400">업종 RS 순위</div>
                <div className="text-base font-bold font-mono text-emerald-400 mt-0.5">
                  {ratings.groupRank}위{' '}
                  <span className="text-[10px] text-slate-400 font-normal">/197개</span>
                </div>
              </div>

              <div className="bg-[#141924] p-2.5 rounded border border-[#20293a]">
                <div className="text-[11px] text-slate-400">A/D 수급 매집 등급</div>
                <div className="text-base font-bold font-mono text-purple-400 mt-0.5">
                  Grade {ratings.adRating}
                </div>
              </div>

              <div className="bg-[#141924] p-2.5 rounded border border-[#20293a]">
                <div className="text-[11px] text-slate-400">SMR 펀더멘털</div>
                <div className="text-base font-bold font-mono text-amber-400 mt-0.5">
                  Grade {ratings.smrRating}
                </div>
              </div>
            </div>

            {/* DART 공시 리스크 배지 */}
            <div className="bg-[#141924] p-2.5 rounded border border-[#20293a] flex items-center justify-between">
              <div className="flex items-center space-x-1.5">
                <AlertCircle size={14} className="text-emerald-400" />
                <span className="text-slate-300 font-medium">DART 공시 리스크</span>
              </div>
              <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-emerald-500/15 text-emerald-400 border border-emerald-500/20">
                {ratings.dartRisk} (안전)
              </span>
            </div>

            {/* Key Valuation Table */}
            <div className="bg-[#141924] rounded border border-[#20293a] divide-y divide-[#1e2637]">
              <div className="p-2 flex justify-between font-mono">
                <span className="text-slate-400">PER (TTM)</span>
                <span className="text-slate-200 font-semibold">{ratings.per}x</span>
              </div>
              <div className="p-2 flex justify-between font-mono">
                <span className="text-slate-400">PBR</span>
                <span className="text-slate-200 font-semibold">{ratings.pbr}x</span>
              </div>
              <div className="p-2 flex justify-between font-mono">
                <span className="text-slate-400">ROE (자기자본이익률)</span>
                <span className="text-slate-200 font-semibold">{ratings.roe}%</span>
              </div>
              <div className="p-2 flex justify-between font-mono">
                <span className="text-slate-400">EPS (주당순이익)</span>
                <span className="text-slate-200 font-semibold">{ratings.eps.toLocaleString()}원</span>
              </div>
            </div>
          </div>
        )}

        {/* TAB 2: AI 진단 리포트 (Why This Candidate) */}
        {activeLeftTab === 'ai' && (
          <div className="space-y-2.5">
            <div className="bg-gradient-to-r from-purple-900/30 to-blue-900/30 p-3 rounded-lg border border-purple-500/30">
              <div className="flex items-center justify-between mb-1.5">
                <span className="text-purple-300 font-bold flex items-center space-x-1">
                  <Sparkles size={13} />
                  <span>Spring AI 종합 소견</span>
                </span>
                <span className="text-[10px] text-emerald-400 font-bold px-1.5 py-0.5 rounded bg-emerald-500/15">
                  {ai.trend}
                </span>
              </div>
              <p className="text-slate-200 leading-relaxed text-[11px]">{ai.summary}</p>
            </div>

            <div className="bg-[#141924] p-2.5 rounded border border-[#20293a]">
              <div className="font-semibold text-blue-400 mb-1">피벗 돌파 분석</div>
              <p className="text-slate-300 text-[11px] leading-relaxed">{ai.breakoutReason}</p>
            </div>

            <div className="bg-[#141924] p-2.5 rounded border border-[#20293a]">
              <div className="font-semibold text-amber-400 mb-1">외인/기관 수급 집중도</div>
              <p className="text-slate-300 text-[11px] leading-relaxed">{ai.flowReason}</p>
            </div>

            <div className="bg-[#141924] p-2.5 rounded border border-red-500/30 bg-red-500/5">
              <div className="font-semibold text-red-400 mb-1">리스크 및 손절선 가이드</div>
              <p className="text-slate-300 text-[11px] leading-relaxed">{ai.riskNotice}</p>
            </div>
          </div>
        )}

        {/* TAB 3: 동종 업종 Top RS 주도주 비교 */}
        {activeLeftTab === 'groupRs' && (
          <div className="space-y-2">
            <div className="text-[11px] text-slate-400 font-medium px-1">
              동일 산업군 내 RS 상대강도 상위 랭킹
            </div>
            <div className="bg-[#141924] rounded border border-[#20293a] overflow-hidden">
              <table className="w-full text-left font-mono">
                <thead className="bg-[#182030] text-[10px] text-slate-400">
                  <tr>
                    <th className="py-1.5 px-2">종목명</th>
                    <th className="py-1.5 px-1 text-center">RS</th>
                    <th className="py-1.5 px-1 text-center">성장</th>
                    <th className="py-1.5 px-2 text-right">등락률</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-[#1e2638] text-[11px]">
                  {groupRs.map((item) => (
                    <tr
                      key={item.symbol}
                      onClick={() => setSelectedSymbol(item.symbol)}
                      className={`cursor-pointer transition-colors ${
                        item.symbol === selectedSymbol
                          ? 'bg-blue-500/15 text-blue-200'
                          : 'hover:bg-[#1a2130] text-slate-300'
                      }`}
                    >
                      <td className="py-1.5 px-2 font-sans font-medium">{item.name}</td>
                      <td className="py-1.5 px-1 text-center font-bold text-blue-400">{item.rs}</td>
                      <td className="py-1.5 px-1 text-center text-slate-400">{item.growth}</td>
                      <td
                        className={`py-1.5 px-2 text-right font-bold ${
                          item.changeRate > 0 ? 'text-red-500' : 'text-blue-400'
                        }`}
                      >
                        {item.changeRate > 0 ? '+' : ''}
                        {item.changeRate}%
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* TAB 4: 연간 재무 및 실적 추이 */}
        {activeLeftTab === 'financials' && (
          <div className="space-y-3">
            <div className="bg-[#141924] p-3 rounded border border-[#20293a] space-y-2">
              <div className="text-slate-300 font-semibold mb-2">실적 성장률 체크리스트 (YoY)</div>
              <div className="flex justify-between text-slate-400 font-mono">
                <span>매출액 성장률</span>
                <span className="text-red-500 font-bold">+14.2%</span>
              </div>
              <div className="flex justify-between text-slate-400 font-mono">
                <span>영업이익 성장률</span>
                <span className="text-red-500 font-bold">+28.5%</span>
              </div>
              <div className="flex justify-between text-slate-400 font-mono">
                <span>순이익 성장률</span>
                <span className="text-red-500 font-bold">+35.1%</span>
              </div>
              <div className="flex justify-between text-slate-400 font-mono">
                <span>영업이익률</span>
                <span className="text-emerald-400 font-bold">12.8%</span>
              </div>
            </div>
          </div>
        )}
      </div>
    </aside>
  )
}
