# Mateus - Classes Concretas de Ação (CombatAction) e Lógica de Turno

### Objetivo

Implementar a lógica de execução interna das ações de combate (`CombatAction`), atuando como despachante de comandos das regras desenvolvidas pelo Gustavo (para movimentos) e pelo Pedro (para itens). Além disso, consolidar as regras de prioridade e validação de trocas de Pokémon em tempo de execução.

### Pacotes e Arquivos (Crie ou Modifique)

* `src/game/battle/CombatAction.java` (Tornar abstrata e revisar base) 
* `src/game/battle/MoveAction.java` (Implementação da execução real) 
* `src/game/battle/ItemAction.java` (Implementação da execução real) 
* `src/game/battle/SwitchAction.java` (Implementação da lógica de troca e validação) 

### O que Implementar (Traduzido do Diagrama com as Diretrizes)

#### 1. Abstração Base

* **`CombatAction` (Classe Abstrata):** Garantir a estrutura base contendo o atributo `actor` (do tipo `Trainer`). As subclasses deverão herdar os métodos e implementar suas respectivas prioridades estáticas de turno.

#### 2. Implementação das Ações como Despachantes (Dispatchers)

* **`MoveAction`:** Esta classe encapsula a intenção de usar um golpe. Ela não deve recalcular o dano, mas sim delegar para a lógica do Gustavo.

* **`getPriority()`:** Deve apenas repassar e retornar a prioridade nativa vinda do objeto `Move` atrelado (`move.getPriority()`).

* **`execute(BattleContext context) -> ActionResult`:** Deve extrair o Pokémon usuário (`user`) e o Pokémon alvo (`target`). Em seguida, invocar diretamente o método desenvolvido pelo Gustavo: `move.execute(user, target, context)`. O resultado dessa execução (`MoveResult`) deve ser mapeado ou traduzido para retornar o `ActionResult` correspondente (como `SUCCESS` ou `MISSED`).

* **`ItemAction`:** Esta classe encapsula o uso de um item do inventário em um Pokémon alvo durante o combate.

* **`getPriority()`:** Deve retornar **prioridade máxima** (por exemplo, `5 ou infinito`), garantindo que a substituição ocorra antes de qualquer comando de ataque em um turno de batalha.

* **`execute(BattleContext context) -> ActionResult`:** Deve recuperar o inventário do treinador que está agindo (`actor.getInventory()`)  e validar com o Pedro se o item está disponível no inventário via `inventory.has(item, 1)`. Se sim, efetuar o consumo físico do item (`inventory.consume(item, 1)`) e disparar o efeito real: `item.use(target)`. Retornar o `ActionResult` produzido pela execução do item.

#### 3. Lógica de Troca e Validação Dedicada

* **`SwitchAction`:** Responsável por substituir o Pokémon ativo em campo por outro do banco de reservas.

* **`getPriority()`:** Deve retornar **prioridade máxima** (por exemplo, `5 ou infinito`), garantindo que a substituição ocorra antes de qualquer comando de ataque em um turno de batalha.

* **`execute(BattleContext context) -> ActionResult`:** 
    1. Acessar o `Team` do treinador atuante (`actor.getTeam()`).
    2. Realizar a validação crucial: buscar o Pokémon na lista de membros correspondente ao índice destino (`targetIndex`)  e checar se ele está vivo (`pokemon.isAlive()`).
    3. Se o Pokémon destino estiver nocauteado ou o índice for inválido, impedir a ação e retornar `ActionResult.INVALID_ACTION`.
    4. Se estiver vivo e apto, invocar o método já existente no time: `team.switchActive(targetIndex)` e retornar `ActionResult.SUCCESS`.

### Dependências

* `game.creature.move.Move` (Gustavo) para invocar a execução e parâmetros de prioridade dos golpes.
* `game.itemsystem.Item` e `Inventory` (Pedro) para validar a posse e o consumo de itens em combate.
* Métodos `isAlive()` de `Pokemon` e `switchActive()` de `Team` (Já existentes).

### Guia do Ciclo

1. **Escrever a Estrutura Base:** Revise o contrato de `CombatAction` e garanta que todas as subclasses herdem corretamente.

2. **Implementar `MoveAction`:** Conecte o método `execute` com a assinatura ajustada do Gustavo, passando o `attacker` (`user`), o `target` e o contexto.

3. **Implementar `ItemAction`:** Insira as checagens e operações de consumo chamando os métodos de mapa e inventário.

4. **Implementar `SwitchAction`:** Crie as travas de segurança. Impeça rigidamente que o jogador consiga trocar para um Pokémon com `isAlive() == false` ou fora dos limites de tamanho do `Team`.


### Referências

* `diagrams/01-battle-core-actions.puml` (Fluxo de combate e ações) 
* `src/game/battle/CombatAction.java` 
