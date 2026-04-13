const API_URL = process.env.NEXT_PUBLIC_API_URL;

export async function activateSubscription(tenantId: string, planName: string): Promise<void> {
  const response = await fetch(`${API_URL}/api/v1/admin/subscriptions/${tenantId}/activate`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ planName }),
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message ?? "activate_failed");
  }
}
