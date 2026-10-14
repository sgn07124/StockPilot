import { useEffect, useRef } from 'react'
import {
  createChart,
  ColorType,
  CandlestickSeries,
  LineSeries,
  HistogramSeries,
} from 'lightweight-charts'
import type {
  IChartApi,
  ISeriesApi,
  CandlestickData,
  LineData,
  HistogramData,
  Time,
} from 'lightweight-charts'
import { useStockStore } from '../store/useStockStore'
import { MOCK_STOCKS, generateMockCandles } from '../services/mockData'

export const MainChart = () => {
  const chartContainerRef = useRef<HTMLDivElement>(null)
  const chartRef = useRef<IChartApi | null>(null)

  const { selectedSymbol, chartLayers, toggleChartLayer } = useStockStore()
  const currentData = MOCK_STOCKS[selectedSymbol] || MOCK_STOCKS['005930']
  const { summary, basePattern } = currentData

  useEffect(() => {
    if (!chartContainerRef.current) return

    // 차트 초기화
    const chart = createChart(chartContainerRef.current, {
      layout: {
        background: { type: ColorType.Solid, color: '#0b0e14' },
        textColor: '#94a3b8',
        fontSize: 11,
      },
      grid: {
        vertLines: { color: '#161d29' },
        horzLines: { color: '#161d29' },
      },
      crosshair: {
        mode: 1, // Magnet mode
        vertLine: { color: '#475569', width: 1, style: 3 },
        horzLine: { color: '#475569', width: 1, style: 3 },
      },
      rightPriceScale: {
        borderColor: '#1e2636',
        scaleMargins: {
          top: 0.1,
          bottom: 0.25,
        },
      },
      timeScale: {
        borderColor: '#1e2636',
        timeVisible: true,
        secondsVisible: false,
      },
    })

    chartRef.current = chart

    // 1. 캔들스틱 시리즈 (v5 addSeries API)
    const candleSeries: ISeriesApi<'Candlestick'> = chart.addSeries(CandlestickSeries, {
      upColor: '#ef4444',
      downColor: '#3b82f6',
      borderUpColor: '#ef4444',
      borderDownColor: '#3b82f6',
      wickUpColor: '#ef4444',
      wickDownColor: '#3b82f6',
    })

    // 2. 이동평균선 시리즈 (SMA 10, 20, 50, 200)
    const ma10Series = chart.addSeries(LineSeries, { color: '#f59e0b', lineWidth: 1, title: 'MA10' })
    const ma20Series = chart.addSeries(LineSeries, { color: '#ef4444', lineWidth: 1, title: 'MA20' })
    const ma50Series = chart.addSeries(LineSeries, { color: '#10b981', lineWidth: 1, title: 'MA50' })
    const ma200Series = chart.addSeries(LineSeries, { color: '#8b5cf6', lineWidth: 2, title: 'MA200' })


    // 3. KOSPI 대비 RS 상대강도선 (블루 실선, 좌측 축)
    const rsLineSeries = chart.addSeries(LineSeries, {
      color: '#38bdf8',
      lineWidth: 2,
      priceScaleId: 'left',
      title: 'RS vs KOSPI',
    })
    chart.priceScale('left').applyOptions({
      visible: true,
      borderColor: '#1e2636',
      scaleMargins: { top: 0.15, bottom: 0.5 },
    })

    // 4. 하단 거래량 시리즈
    const volumeSeries = chart.addSeries(HistogramSeries, {
      color: '#26a69a',
      priceFormat: { type: 'volume' },
      priceScaleId: '', // Overlay mode
    })
    volumeSeries.priceScale().applyOptions({
      scaleMargins: { top: 0.8, bottom: 0 },
    })

    // 5. 목 캔들 데이터 로드
    const rawCandles = generateMockCandles(summary.currentPrice)

    const candleData: CandlestickData<Time>[] = rawCandles.map((c) => ({
      time: c.time as Time,
      open: c.open,
      high: c.high,
      low: c.low,
      close: c.close,
    }))

    const ma10Data: LineData<Time>[] = rawCandles.filter((c) => c.ma10 !== undefined).map((c) => ({ time: c.time as Time, value: c.ma10! }))
    const ma20Data: LineData<Time>[] = rawCandles.filter((c) => c.ma20 !== undefined).map((c) => ({ time: c.time as Time, value: c.ma20! }))
    const ma50Data: LineData<Time>[] = rawCandles.filter((c) => c.ma50 !== undefined).map((c) => ({ time: c.time as Time, value: c.ma50! }))
    const ma200Data: LineData<Time>[] = rawCandles.filter((c) => c.ma200 !== undefined).map((c) => ({ time: c.time as Time, value: c.ma200! }))

    const rsData: LineData<Time>[] = rawCandles.filter((c) => c.rsLine !== undefined).map((c) => ({ time: c.time as Time, value: c.rsLine! }))

    const volumeData: HistogramData<Time>[] = rawCandles.map((c) => ({
      time: c.time as Time,
      value: c.volume,
      color: c.close >= c.open ? 'rgba(239, 68, 68, 0.45)' : 'rgba(59, 130, 246, 0.45)',
    }))

    candleSeries.setData(candleData)
    ma10Series.setData(ma10Data)
    ma20Series.setData(ma20Data)
    ma50Series.setData(ma50Data)
    ma200Series.setData(ma200Data)
    rsLineSeries.setData(rsData)
    volumeSeries.setData(volumeData)

    // 6. 베이스 패턴 가이드라인 (Pivot, Buy +5%, Risk, Target)
    if (chartLayers.levels && basePattern) {
      candleSeries.createPriceLine({
        price: basePattern.pivotPrice,
        color: '#10b981',
        lineWidth: 2,
        lineStyle: 2, // Dashed
        axisLabelVisible: true,
        title: `Pivot (${basePattern.pivotPrice.toLocaleString()})`,
      })

      candleSeries.createPriceLine({
        price: basePattern.buyUpperPrice,
        color: '#34d399',
        lineWidth: 1,
        lineStyle: 3, // Dotted
        axisLabelVisible: true,
        title: `Buy +5% (${basePattern.buyUpperPrice.toLocaleString()})`,
      })

      candleSeries.createPriceLine({
        price: basePattern.riskPrice,
        color: '#f87171',
        lineWidth: 2,
        lineStyle: 2,
        axisLabelVisible: true,
        title: `Risk 손절 (${basePattern.riskPrice.toLocaleString()})`,
      })

      candleSeries.createPriceLine({
        price: basePattern.targetPrice,
        color: '#38bdf8',
        lineWidth: 2,
        lineStyle: 0, // Solid
        axisLabelVisible: true,
        title: `Target 목표 (${basePattern.targetPrice.toLocaleString()})`,
      })
    }

    // 반응형 리사이징
    const handleResize = () => {
      if (chartContainerRef.current) {
        chart.applyOptions({
          width: chartContainerRef.current.clientWidth,
          height: chartContainerRef.current.clientHeight,
        })
      }
    }

    window.addEventListener('resize', handleResize)
    chart.timeScale().fitContent()

    return () => {
      window.removeEventListener('resize', handleResize)
      chart.remove()
    }
  }, [selectedSymbol, summary.currentPrice, chartLayers.levels])

  return (
    <div className="flex-1 flex flex-col h-full bg-[#0b0e14] relative overflow-hidden select-none">
      {/* Chart Layer Control Toolbar */}
      <div className="h-9 bg-[#111622] border-b border-[#1c2331] px-3 flex items-center justify-between text-xs">
        <div className="flex items-center space-x-1.5 font-medium">
          <span className="text-[11px] text-slate-500 font-semibold mr-1">LAYERS:</span>

          <button
            onClick={() => toggleChartLayer('ma')}
            className={`px-2 py-0.5 rounded text-[11px] transition-all ${
              chartLayers.ma
                ? 'bg-blue-600/20 text-blue-400 border border-blue-500/30'
                : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            이평선 (10/20/50/200)
          </button>

          <button
            onClick={() => toggleChartLayer('rs')}
            className={`px-2 py-0.5 rounded text-[11px] transition-all ${
              chartLayers.rs
                ? 'bg-cyan-500/20 text-cyan-400 border border-cyan-500/30'
                : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            RS 라인 (vs KOSPI)
          </button>

          <button
            onClick={() => toggleChartLayer('levels')}
            className={`px-2 py-0.5 rounded text-[11px] transition-all ${
              chartLayers.levels
                ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/30'
                : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            베이스 레벨 (피벗/손절)
          </button>

          <button
            onClick={() => toggleChartLayer('volume')}
            className={`px-2 py-0.5 rounded text-[11px] transition-all ${
              chartLayers.volume
                ? 'bg-amber-500/20 text-amber-400 border border-amber-500/30'
                : 'text-slate-500 hover:text-slate-300'
            }`}
          >
            거래량 바
          </button>
        </div>

        {/* Pattern Badge Info */}
        <div className="hidden sm:flex items-center space-x-2 text-[11px]">
          <span className="px-2 py-0.5 rounded bg-blue-500/10 text-blue-400 border border-blue-500/20 font-mono">
            {basePattern.weeks}주 베이스 · 저점 {basePattern.lowDropPct}%
          </span>
          <span className="px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 font-mono">
            Pivot: {basePattern.pivotPrice.toLocaleString()}원
          </span>
        </div>
      </div>

      {/* Lightweight Chart Render Canvas */}
      <div ref={chartContainerRef} className="flex-1 w-full h-full min-h-[380px]" />
    </div>
  )
}
