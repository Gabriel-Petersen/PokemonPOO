# Mateus - Contratos base de batalha e interacao

Objetivo
- Criar os contratos minimos de treinador e batalha no pacote `game.battle`, e o contrato de interacao em `game.shared`.

Pacotes e arquivos (crie se nao existir)
- `src/game/battle/ActionResult.java`-> OK
- `src/game/battle/BattleContext.java`-> OK
- `src/game/battle/Trainer.java`-> OK
- `src/game/battle/NpcTrainer.java`
- `src/game/battle/WildTrainer.java`
- `src/game/battle/Team.java`-> OK
- `src/game/player/Interactable.java`-> OK

O que implementar (traduzido do diagrama)
- `ActionResult`: `SUCCESS`, `FAILED`, `INVALID_ACTION`, `MISSED`, `FLED`, `CAPTURED`.
- `BattleContext`: contexto leve usado nas assinaturas de acao (sem logica completa).
- `Team`: lista de `Pokemon`, `activeIndex`; metodos `getActiveMember`, `hasAvailableMember`, `switchActive`.
- `Trainer`: `getDisplayName`, `getTeam`, `isWild`, `selectAction(BattleContext)`.
- `NpcTrainer` e `WildTrainer`: dados simples (`displayName`, `team`, `wild`) + `selectAction` stub.
- `Interactable`: interface com `onInteract(Player)`.

Dependencias
- `game.creature.Pokemon` (Gustavo) para `Team`.
- `game.player.Player` (Gabriel) para a assinatura de `onInteract`.

Guia do ciclo
1. Criar `ActionResult` e `BattleContext`.
2. Implementar `Team` com lista de `Pokemon`.
3. Criar `Trainer`, `NpcTrainer` e `WildTrainer` com implementacoes simples.
4. Criar `Interactable` em `game.shared`.

Referencias
- `diagrams/01-battle-core-actions.puml`
- `diagrams/04-exploration-encounter.puml`
- `diagrams/05-domain-engine-integration.puml`
- `docs/01-visao-geral-da-engine.md`