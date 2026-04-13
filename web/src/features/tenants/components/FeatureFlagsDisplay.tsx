import { Tag } from "antd";
import type { FeatureFlags } from "../types/tenant.types";

const FLAG_LABELS: Record<keyof FeatureFlags, string> = {
  sizeVariants: "Variantes de tamaño",
  modifiers: "Modificadores",
  combos: "Combos",
  stockControl: "Control de stock",
  ingredientRecipes: "Recetas / Ingredientes",
  customerDisplay: "Display cliente",
  ticketPrinting: "Impresión de tickets",
};

interface Props {
  flags: FeatureFlags;
}

export function FeatureFlagsDisplay({ flags }: Props) {
  return (
    <div style={{ display: "flex", flexWrap: "wrap", gap: 8 }}>
      {(Object.keys(FLAG_LABELS) as (keyof FeatureFlags)[]).map((key) => (
        <Tag key={key} color={flags[key] ? "green" : "default"}>
          {FLAG_LABELS[key]}
        </Tag>
      ))}
    </div>
  );
}
