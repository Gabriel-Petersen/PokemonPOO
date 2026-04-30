# Pedro - Itens, inventario e comercio basico

Objetivo
- Implementar itens e inventario no pacote `game.items`, com contratos basicos de vendedor em `game.commerce`.

Pacotes e arquivos (crie se nao existir)
- `src/game/items/Item.java`
- `src/game/items/HealingItem.java`
- `src/game/items/HpHealItem.java`
- `src/game/items/StatusHealItem.java`
- `src/game/items/CaptureItem.java`
- `src/game/items/Inventory.java`
- `src/game/items/ItemStack.java`
- `src/game/commerce/Seller.java`
- `src/game/commerce/NpcSeller.java`
- `src/game/commerce/ShopService.java` (stub)

O que implementar (traduzido do diagrama)
- `Item`: `name`, `description`, `battleUsable`, `basePrice`; metodos `canUse(target: Pokemon)` e `use(target: Pokemon) -> ActionResult`.
- `HealingItem`: `healAmount`; herda de `Item`.
- `HpHealItem` e `StatusHealItem`: sobrescrevem `canUse`/`use` com retorno simples.
- `CaptureItem`: `captureModifier`; `computeCatchChance(target: Pokemon)` e `use(target: Pokemon) -> ActionResult`.
- `Inventory`: lista de `ItemStack`; metodos `count`, `has`, `add`, `consume`.
- `ItemStack`: `item` + `amount`; metodos `increase`, `decrease`, `canConsume`.
- `Seller`: `listItemsForSale`.
- `NpcSeller`: `displayName`, `catalog`, `unlimitedStock`; implementa `Seller`.
- `ShopService`: `buyItem`, `sellItem`, `calculatePrice` (stub; sem logica final).

Dependencias
- `game.battle.ActionResult` (Mateus) e `game.creature.Pokemon` (Gustavo) para assinaturas.
- `game.battle.Trainer` apenas se precisar de referencia (opcional nesta fase).

Guia do ciclo
1. Criar `Item` e `HealingItem` com campos e assinaturas.
2. Criar `HpHealItem`, `StatusHealItem` e `CaptureItem` com stubs.
3. Criar `Inventory` e `ItemStack` com operacoes simples.
4. Criar `Seller` e `NpcSeller` com catalogo basico.
5. Criar `ShopService` com metodos stub.

Referencias
- `diagrams/03-items-inventory-capture.puml`
- `diagrams/01-battle-core-actions.puml` (`ActionResult`)
- `docs/01-visao-geral-da-engine.md` (contexto de arquitetura)