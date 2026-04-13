"use client";

export const dynamic = "force-dynamic";

import { Suspense } from "react";
import { Providers } from "@/shared/components/Providers";
import { ThemedLayout, ThemedTitle } from "@refinedev/antd";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return (
    <Suspense>
      <Providers>
        <ThemedLayout
          Title={() => <ThemedTitle collapsed={false} text="POS SaaS Admin" />}
        >
          {children}
        </ThemedLayout>
      </Providers>
    </Suspense>
  );
}