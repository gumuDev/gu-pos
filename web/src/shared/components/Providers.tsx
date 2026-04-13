"use client";

import { Refine } from "@refinedev/core";
import routerProvider from "@refinedev/nextjs-router";
import { adminDataProvider } from "@/shared/services/adminDataProvider";

export function Providers({ children }: { children: React.ReactNode }) {
  return (
    <Refine
      dataProvider={adminDataProvider}
      routerProvider={routerProvider}
      resources={[
        {
          name: "tenants",
          list: "/admin/tenants",
          show: "/admin/tenants/:id",
          meta: {
            label: "Negocios",
          },
        },
        {
          name: "subscriptions",
          list: "/admin/subscriptions",
          meta: {
            label: "Suscripciones",
          },
        },
        {
          name: "subscription-requests",
          list: "/admin/subscription-requests",
          meta: {
            label: "Solicitudes",
          },
        },
        {
          name: "support-reports",
          list: "/admin/support",
          meta: {
            label: "Soporte",
          },
        },
      ]}
      options={{ disableTelemetry: true }}
    >
      {children}
    </Refine>
  );
}
