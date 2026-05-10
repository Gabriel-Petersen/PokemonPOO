# Gabriel - Integracao e UI de pausa

Objetivo
- Integrar a pausa navegavel, a aba de metadados do player e os NPCs basicos, usando somente `engine.ui` (sem Swing direto).

Pacotes e arquivos (crie se nao existir)
- `src/game/player/Player.java` (ajustes) -> falta animação de "correndo"
- `src/game/ui/PauseMenu.java` -> 80% OK
- `src/game/ui/PauseTab.java` -> OK
- `src/game/ui/PlayerMetadataTab.java` -> OK (adaptado)
- `src/game/integration/Npc.java`
- `src/execs/Main.java` (ajustes)

O que implementar (traduzido do diagrama)
- `Player`: manter campos de metadados (nome e dinheiro) e toggle de pausa no input.
- `PauseMenu` + `PauseTab`: UI navegavel com abas; so a aba de metadados precisa mostrar dados reais no ciclo 1.
- `PlayerMetadataTab`: mostra nome e dinheiro com `UiText`.
- `Npc`: `GameObject` com `onInteract(Player)` exibindo UI de "loja WIP" via `UiText` ou `UiImage`.

Dependencias
- `game.shared.Interactable` e `game.battle.NpcTrainer` (Mateus).
- `game.commerce.NpcSeller` (Pedro) se o NPC for de loja.

Guia do ciclo
1. Ajustar `Player` com nome, dinheiro e toggle de pausa.
2. Criar `PauseMenu` e `PauseTab` seguindo os docs de UI.
3. Criar `PlayerMetadataTab` mostrando dados reais.
4. Implementar `Npc` com `onInteract` exibindo UI "loja WIP".
5. Ajustar `Main` para instanciar `Player` e 1 NPC de teste.

Referencias
- `diagrams/05-domain-engine-integration.puml`
- `docs/03-interface-do-usuario-ui.md`
- `src/examples/UiElementExamples.java`
- `src/game/player/Player.java`
- `src/execs/Main.java`