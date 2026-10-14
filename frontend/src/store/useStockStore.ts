import { create } from 'zustand'

export interface ChartLayerState {
  price: boolean;
  ma: boolean;
  rs: boolean;
  levels: boolean;
  volume: boolean;
  flow: boolean;
}

interface StockStore {
  selectedSymbol: string;
  timeframe: '1D' | '1W' | '1M' | '1h' | '15m';
  activeLeftTab: 'ratings' | 'ai' | 'groupRs' | 'financials';
  chartLayers: ChartLayerState;
  screenerCategory: string;
  scoreTier: 'ALL' | 'B+' | 'A+' | 'S';
  
  setSelectedSymbol: (symbol: string) => void;
  setTimeframe: (timeframe: '1D' | '1W' | '1M' | '1h' | '15m') => void;
  setActiveLeftTab: (tab: 'ratings' | 'ai' | 'groupRs' | 'financials') => void;
  toggleChartLayer: (layer: keyof ChartLayerState) => void;
  setScreenerCategory: (category: string) => void;
  setScoreTier: (tier: 'ALL' | 'B+' | 'A+' | 'S') => void;
}

export const useStockStore = create<StockStore>((set) => ({
  selectedSymbol: '005930', // 삼성전자
  timeframe: '1D',
  activeLeftTab: 'ratings',
  chartLayers: {
    price: true,
    ma: true,
    rs: true,
    levels: true,
    volume: true,
    flow: true,
  },
  screenerCategory: 'desk_promising',
  scoreTier: 'ALL',

  setSelectedSymbol: (symbol) => set({ selectedSymbol: symbol }),
  setTimeframe: (timeframe) => set({ timeframe }),
  setActiveLeftTab: (activeLeftTab) => set({ activeLeftTab }),
  toggleChartLayer: (layer) =>
    set((state) => ({
      chartLayers: {
        ...state.chartLayers,
        [layer]: !state.chartLayers[layer],
      },
    })),
  setScreenerCategory: (screenerCategory) => set({ screenerCategory }),
  setScoreTier: (scoreTier) => set({ scoreTier }),
}))
