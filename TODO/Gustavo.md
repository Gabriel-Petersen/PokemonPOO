# Gustavo - Status Effects, Refatoração e Lógica Concreta de Moves

### Objetivo

Consolidar a infraestrutura de efeitos de status do pacote `game.creature.move.status` e implementar a lógica real e a matemática de cálculo de dano e aplicação de status para os movimentos (`Move`) no projeto.

### Pacotes e Arquivos (Crie ou Modifique)

* `src/game/creature/move/status/StatusEffect.java` (Finalização da interface) 
* `src/game/creature/move/status/StatModifier.java` 
* `src/game/creature/move/status/StatModifierRule.java` 
* `src/game/creature/move/status/VolatileStatusEffect.java` (Implementação com regra de teste) 
* `src/game/creature/move/Move.java` (Tornar abstrata e incluir assinaturas) 
* `src/game/creature/move/MoveResult.java` (Ajuste de campos) 
* `src/game/creature/move/DamageMove.java` (Cálculo real de dano físico/especial) 
* `src/game/creature/move/StatusMove.java` (Aplicação do efeito) 
* `src/game/creature/move/MoveCategory.java` (Ajuste do enum) 

### O que Implementar (Traduzido do Novo Diagrama com Ajustes)

#### 1. Infraestrutura de Status (O Primeiro Passo)

* **`StatusEffect` (Interface):** Garantir a existência de todas as assinaturas do diagrama: `onApply`, `onTurnStart`, `onTurnEnd`, `getStatModifierRules` e `isExpired`.


* **`StatModifierRule`:** Classe que vincula um `StatType` a uma expressão lambda funcional `StatModifier`. Deve conter o método `applyOn(Stats baseStats, BattleContext context)` que processa e devolve um novo objeto `Stats` modificado.


* **`VolatileStatusEffect` (Implementação de Teste):** Deve gerenciar o contador `remainingTurns`. Instancie uma regra de modificação de status de teste (ex: um modificador via expressão lambda que reduz a velocidade (`SPEED`) do Pokémon afetado em $20\%$) e registre-a na lista interna retornada por `getStatModifierRules()`.


#### 2. Modelagem Base de Movimentos e Correções

* **`MoveCategory` (Enum):** Remover a opção `STATUS` do enum. O enum deve conter **apenas** `PHYSICAL` e `SPECIAL`. (A natureza de alteração de status agora é definida unicamente pelo tipo da classe do movimento: `StatusMove`) .


* **`Move` (Classe Abstrata):** Adicionar todos os campos do diagrama (`name`, `power`, `accuracy`, `priority`, `elementType`). A assinatura do método principal deve ser alterada para:
`public abstract MoveResult execute(Pokemon attacker, Pokemon target, BattleContext context);` *(Inclusão do argumento `attacker` para identificação de quem executa a ação).*


* **`MoveResult`:** Deve conter os campos booleanos de acerto, dano aplicado, se o status foi aplicado, a lista de mensagens de log (`List<String>`), e o novo campo **`private Pokemon attacker;`** para registrar o autor do movimento. Os logs são as frases que vão ser vistas pelo player ao longo do combate. Logo, devem incluir dados como "quem atacou" e o nome do ataque. Ex: "Soussomón usou ÁrvoreAVL em Salvalmón"



#### 3. Classes Concretas e Mecânica de Dano

* **`DamageMove` (Movimentos de Ataque):** Deve ler o seu `MoveCategory` (`PHYSICAL` ou `SPECIAL`). O método `execute` deve realizar o cálculo matemático real de dano:

1. Identificar os atributos relevantes com base na categoria:
* Se `PHYSICAL`: Usa o `ATTACK` do atacante e a `DEFENSE` do alvo.
* Se `SPECIAL`: Usa o `SPECIAL_ATTACK` do atacante e a `SPECIAL_DEFENSE` do alvo.

2. Aplicar a fórmula personalizada estabelecida:

$$\text{Dano} = \max\left(0, \; \text{power\_do\_move} + \text{stat\_relevante\_do\_atacante} - \frac{\text{defesa\_relevante\_do\_alvo}}{2}\right)$$

3. Invocar `target.receiveDamage(dano)` e retornar um `MoveResult` preenchido com o dano causado.


* **`StatusMove` (Movimentos de Efeito):** Deve carregar uma instância ou referência de `StatusEffect`. O método `execute` deve invocar `target.applyStatus(this.effect, context)` e devolver um `MoveResult` indicando se a aplicação foi bem-sucedida ou se falhou.


### Dependências

* `game.battle.BattleContext` (Mateus) apenas para as assinaturas dos métodos.

* Métodos `receiveDamage` e `applyStatus` da classe `Pokemon` (Já criados por você no Ciclo 1).


### Guia do Ciclo

1. **Estabelecer a Interface e Regras:** Finalize `StatusEffect` e implemente `StatModifierRule`. Crie o comportamento do modificador de velocidade na classe `VolatileStatusEffect`.


2. **Ajustar Enums e Contrato de Move:** Altere `MoveCategory` e transforme `Move` em uma classe abstrata contendo as assinaturas ajustadas com o parâmetro `attacker`.


3. **Desenvolver o Cálculo em DamageMove:** Implemente a lógica que diferencia dano físico de especial e aplique rigorosamente a fórmula matemática usando os status efetivos do alvo e do atacante.


4. **Desenvolver o StatusMove:** Conecte a aplicação do efeito de status ao método de recepção existente na classe `Pokemon`.


### Referências

* `diagrams/02-creatures-moves-status.puml`
* `src/game/creature/move/Move.java` 
* `src/game/creature/move/status/StatusEffect.java`