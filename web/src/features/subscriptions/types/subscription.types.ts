export interface SubscriptionSummary {
  tenantId: string;
  tenantName: string;
  tenantPhone: string;
  planName: string;
  subscriptionStatus: string | null;
  subscriptionEndsAt: string | null;
}
