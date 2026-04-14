# Fila de Eventos

Este documento explica a fila de eventos da engine: como ela funciona, como você coloca eventos nela e como criar seus próprios eventos.

A ideia principal é simples: você monta uma sequência de ações e a engine executa uma por vez, na ordem certa, ao longo do tempo.

Isso combina muito bem com um jogo de RPG de combate em turnos, onde um turno inteiro pode ser resolvido em um único frame do ponto de vista da lógica, mas suas consequências visuais aparecem aos poucos na tela.

---

## 1) O que é a fila de eventos?

A fila de eventos é um mecanismo para executar ações em sequência.

Em vez de fazer tudo de uma vez, você coloca vários eventos na fila e depois inicia a resolução dessa fila.

Exemplos de coisas que podem virar eventos:

- mostrar um texto por alguns segundos,
- esperar um tempo,
- mudar a barra de vida aos poucos,
- tocar uma animação específica,
- executar uma ação instantânea no meio da sequência.

---

## 2) Como ela funciona

O centro disso é o `EventScheduler`.

Ele guarda uma fila de eventos e trabalha de forma **FIFO**:

- o primeiro evento que entra é o primeiro a ser executado,
- o próximo só começa quando o anterior terminar,
- não há execução paralela dentro da mesma fila.

### Ciclo básico

1. Você cria o scheduler.
2. Você adiciona eventos com `enqueue(...)`.
3. Quando tudo estiver pronto, chama `resolve()`.
4. A cada frame, o `GamePanel` chama `update(...)` do scheduler.
5. O scheduler executa o evento atual até ele terminar.
6. Quando termina, ele passa para o próximo.

### Regra importante

Se a fila estiver vazia, ela para automaticamente.

---

## 3) Características principais

### Ordem fixa

Os eventos são executados na ordem em que foram enfileirados.

Isso é muito útil quando você quer uma sequência como:

1. mostrar ataque,
2. esperar um pouco,
3. alterar a vida,
4. mostrar dano,
5. mostrar o resultado.

### Um evento por vez

A fila trabalha com uma lógica clara: **um evento termina, o próximo começa**.

Isso deixa o fluxo mais fácil de entender e de debugar.

### Baseada em tempo

Os eventos recebem `deltaTime` no `update(double dt)`.

Isso permite criar ações que duram alguns segundos sem travar o jogo.

### Boa para cenas guiadas

Ela é ótima para situações em que você quer controlar a sequência visual de forma muito precisa:

- batalha em turnos,
- diálogo com caixas de texto,
- efeitos de ataque,
- transições de tela.

---

## 4) Relação com o loop principal

A fila de eventos não roda sozinha.

Ela é atualizada dentro do ciclo principal do jogo.

Na prática:

- o jogo calcula o tempo do frame,
- o `GamePanel` atualiza o mundo,
- se houver fila ativa, os eventos daquela fila avançam.

Isso significa que a engine continua sendo guiada pelo loop principal, mas os eventos controlam uma parte especial da atualização.

---

## 5) Como criar e configurar eventos na fila

O modelo é sempre parecido:

```java
EventScheduler scheduler = new EventScheduler();

scheduler.enqueue(new PrimeiroEvento(...));
scheduler.enqueue(new SegundoEvento(...));
scheduler.enqueue(new TerceiroEvento(...));

scheduler.resolve();
```

### Regras práticas

- coloque os eventos na ordem desejada,
- só chame `resolve()` depois de enfileirar tudo,
- se a fila já estiver resolvendo, normalmente você evita adicionar a mesma sequência de novo.

No exemplo `examples.EventQueue`, isso aparece quando o botão monta toda a sequência de ataque e só depois inicia a execução.

---

## 6) Modelo básico de um evento

Todos os eventos seguem o mesmo formato-base: eles herdam de `GameEvent`.

Esse tipo exige três métodos:

- `init()` → roda no começo do evento,
- `update(double dt)` → roda enquanto o evento está ativo,
- `isFinished()` → diz quando o evento terminou.

### Como pensar nisso

- `init()` = preparação inicial,
- `update(dt)` = comportamento ao longo do tempo,
- `isFinished()` = condição de término.

---

## 7) Exemplo de evento personalizado

No arquivo `examples.EventQueue`, existe um exemplo de evento próprio chamado `TemporaryMessageEvent`.

Ele serve para mostrar o modelo básico de criação de eventos personalizados.

A ideia dele é simples:

- entra com uma mensagem,
- mostra essa mensagem por um tempo,
- depois limpa o texto.

### Estrutura mental do evento

```java
private static class TemporaryMessageEvent extends GameEvent
{
    private final UiText target;
    private final String message;
    private final double duration;
    private double elapsed = 0;

    public TemporaryMessageEvent(UiText target, String message, double duration) {
        super(null);
        this.target = target;
        this.message = message;
        this.duration = duration;
    }

    @Override
    public void init() {
        elapsed = 0;
        target.setText(message);
    }

    @Override
    public void update(double dt) {
        elapsed += dt;
        if (elapsed >= duration) {
            target.setText("");
        }
    }

    @Override
    public boolean isFinished() {
        return elapsed >= duration;
    }
}
```

### O que isso mostra

Esse modelo é ótimo porque dá para adaptar facilmente para qualquer coisa:

- mudar cor de UI,
- mostrar aviso temporário,
- ligar/desligar um efeito,
- disparar uma animação,
- aplicar dano depois de um delay.

---

## 8) Ordem de eventos no exemplo de combate

No exemplo `EventQueue`, a sequência segue uma lógica parecida com um turno de RPG:

1. mostra uma mensagem curta de preparação,
2. exibe o texto do ataque,
3. espera um pouco,
4. muda a cor da barra de vida,
5. reduz a vida aos poucos,
6. mostra o resultado do dano,
7. restaura a interface para o próximo passo.

Essa forma de organizar os eventos deixa o combate mais claro e mais bonito visualmente.

A lógica do turno pode acontecer “de uma vez” no código, mas o resultado visual vai aparecendo com calma na tela.

---

## 9) Tipos de eventos já usados na engine

A engine já tem alguns eventos úteis prontos:

### `WaitEvent`

Espera um tempo antes de terminar.

### `LambdaEvent`

Executa uma ação curta e direta.

É útil quando você quer fazer algo simples no meio da fila.

### `TypewriterEvent`

Escreve um texto aos poucos, como se estivesse sendo digitado.

### `ProgressBarChangeEvent`

Muda o valor da barra de progresso gradualmente.

### `AnimationEvent`

Troca uma animação e depois volta para a anterior.

---

## 10) Quando usar a fila de eventos

Use a fila quando você quiser controlar bem a ordem visual das coisas.

Ela é muito boa para:

- batalhas em turnos,
- cenas de vitória/derrota,
- introdução de habilidade,
- dano e feedback visual,
- mensagens guiadas ao jogador.

Se você só quer movimentação livre ou lógica contínua, talvez nem precise dela o tempo todo.

---

## 11) Exemplo de leitura mental do fluxo

Imagine este turno:

- o jogador escolhe “Ataque”,
- a engine calcula o resultado inteiro de uma vez,
- monta a fila:
  - mensagem inicial,
  - animação de ataque,
  - pausa curta,
  - perda de vida,
  - texto de resultado,
  - fim do turno.

O cálculo é rápido, mas a apresentação visual acontece em ordem, sem parecer tudo amontoado.

---

## 12) Resumo prático

Para usar a fila de eventos:

1. crie um `EventScheduler`,
2. adicione eventos com `enqueue(...)`,
3. chame `resolve()`,
4. deixe a engine atualizar a fila a cada frame.

Para criar um evento novo:

1. herde de `GameEvent`,
2. implemente `init()`, `update(dt)` e `isFinished()`,
3. coloque o evento na fila junto com os demais.

---

## Fechamento

A fila de eventos existe para deixar o jogo mais controlado e mais bonito visualmente, principalmente em batalhas por turnos.

Ela separa duas coisas importantes:

- **cálculo do turno**,
- **apresentação do turno**.

Isso ajuda muito a manter o código organizado e a experiência do jogador mais clara.
