export interface TenantSummary {
  id: string;
  name: string;
  phone: string;
  planName: string;
  createdAt: string;
}

export interface FeatureFlags {
  sizeVariants: boolean;
  modifiers: boolean;
  combos: boolean;
  stockControl: boolean;
  ingredientRecipes: boolean;
  customerDisplay: boolean;
  ticketPrinting: boolean;
}

export interface SubscriptionHistoryItem {
  id: string;
  planId: string;
  status: string;
  startedAt: string;
  endsAt: string | null;
  activatedAt: string;
}

export interface TenantDetail {
  id: string;
  name: string;
  phone: string;
  currency: string;
  businessType: string;
  status: string;
  createdAt: string;
  featuresConfig: FeatureFlags;
  planId: string;
  planName: string;
  subscriptionStatus: string | null;
  subscriptionStartedAt: string | null;
  subscriptionEndsAt: string | null;
  subscriptionHistory: SubscriptionHistoryItem[];
  branchId: string;
  branchName: string;
}
