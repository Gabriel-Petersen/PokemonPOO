# Pedro - Itens, inventario e comercio basico

Objetivo
- Implementar itens e inventario no dominio, com contratos basicos de vendedor.

Pacotes e arquivos (crie se nao existir)
- src/domain/items/Item.java
- src/domain/items/HealingItem.java
- src/domain/items/HpHealItem.java
- src/domain/items/StatusHealItem.java
- src/domain/items/CaptureItem.java
- src/domain/items/Inventory.java
- src/domain/items/ItemStack.java
- src/domain/commerce/Seller.java
- src/domain/commerce/NpcSeller.java
- src/domain/commerce/ShopService.java (stub)

O que implementar (traduzido do diagrama)
- Item: name, description, battleUsable, basePrice; metodos canUse(target: Pokemon) e use(target: Pokemon) -> ActionResult.
- HealingItem: healAmount; herda de Item.
- HpHealItem e StatusHealItem: sobrescrevem canUse/use com retorno simples.
- CaptureItem: captureModifier; computeCatchChance(target: Pokemon) e use(target: Pokemon) -> ActionResult (sem logica real).
- Inventory: lista de ItemStack; metodos count, has, add, consume.
- ItemStack: item + amount; metodos increase, decrease, canConsume.
- Seller interface: listItemsForSale.
- NpcSeller: displayName, catalog, unlimitedStock; implementa Seller.
- ShopService: buyItem, sellItem, calculatePrice (stub; sem logica final).

Dependencias
- ActionResult (Mateus) e Pokemon (Gustavo) para assinaturas.
- Trainer apenas se precisar de referencia (opcional nesta fase).

Guia do ciclo
1. Criar Item e HealingItem com campos e assinaturas.
2. Criar HpHealItem, StatusHealItem e CaptureItem com stubs.
3. Criar Inventory e ItemStack com operacoes simples.
4. Criar Seller e NpcSeller com catalogo basico.
5. Criar ShopService com metodos stub.

Referencias
- diagrams/03-items-inventory-capture.puml
- diagrams/01-battle-core-actions.puml (ActionResult)
- docs/01-visao-geral-da-engine.md (contexto de arquitetura)
