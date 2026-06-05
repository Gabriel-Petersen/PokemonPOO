# Pedro - Inventário, Refatoração e Comércio Puto

### Objetivo

Refatorar a estrutura interna do inventário para usar um mapa, implementar a lógica robusta de manipulação de itens em memória e consolidar o serviço de compra e venda (comércio) no pacote `game.commerce`.

### Pacotes e Arquivos (Crie ou Modifique)

* `src/game/itemsystem/Inventory.java`

* `src/game/itemsystem/ItemStack.java` (Validações auxiliares -> verifique se está tudo certo)

* `src/game/commerce/Seller.java`

* `src/game/commerce/ShopService.java` (Implementação real da lógica)

### O que Implementar 

#### 1. `Inventory`

Implementar a classe para gerir ItemStacks, com métodos para manipulação segura, obtenção de quantidades, acréscimo e decréscimo de itens. Pesquise sobre a função `putIfAbsent()` da interface `Map<K, V>`do Java e aplique-a.

* **Método `add(Item item, int amount)`:** Deve verificar se o `Item` já existe como chave no mapa. Se sim, recupera o `ItemStack` e incrementa a quantidade. Se não, instancia um novo `ItemStack` e o insere no mapa.
* **Método `consume(Item item, int amount) -> Boolean`:** Deve verificar se o item existe e se a quantidade disponível no `ItemStack` é suficiente. Se sim, decrementa a quantidade. Se o total chegar a zero, remova o item do mapa para manter a coleção limpa. Retorne `true` se o consumo foi bem-sucedido e `false` caso contrário.
* **Método `has(Item item, int amount) -> Boolean`:** Retorna se o mapa contém o item com uma quantidade maior ou igual à solicitada.
* **Método `count(Item item) -> Integer`:** Retorna a quantidade total daquele item específico (buscando no valor do `ItemStack`).

#### 2. `Seller`

* **`Seller` (Interface):** O método `listItemsForSale()` deve retornar uma coleção ou lista de `ItemStack`, permitindo que itens com estoque limitado sejam exibidos corretamente com suas respectivas quantidades. Prototipe também o método `getInventory()`

#### 3. `ShopService` (Lógica de Comércio Direta)

* **Método `buyItem(Player player, Seller seller, Item item, int amount) -> ActionResult`:**
1. Verifica se o `seller` possui o item disponível na quantidade desejada através do `getInventory()`.
2. Calcula o custo total: $custo = item.getBasePrice() \times amount \times buyMultiplier$.
3. Verifica se o jogador tem dinheiro suficiente (`player.getMetadata().getMoney() >= custo`).
4. Se válido: deduz o dinheiro do jogador, consome o estoque do vendedor (se não for ilimitado) e adiciona os itens ao `Inventory` do jogador.


* **Método `sellItem(Player player, Seller seller, Item item, int amount) -> boolean`:**
1. Verifica se o jogador possui o item na quantidade desejada em seu `Inventory`.
2. Calcula o ganho total: $Ganho = item.getBasePrice() \times amount \times sellMultiplier$.
3. Se válido: remove os itens do inventário do jogador e adiciona o valor de `Ganho` ao saldo de dinheiro do jogador.

* A constante `private final Double buyMultiplier = ?` deve ser **maior** que 1.0
* A constante `private final Double sellMultiplier = ?` deve ser **menor** que 1.0

### Dependências

* `game.player.Player` (Gabriel) para ler e alterar o saldo de dinheiro de `PlayerMetadata`.
* `game.itemsystem.Item` e subclasses (Já implementados com sucesso por você).

### Guia do Ciclo

1. **Começe por `Inventory`:** Guarde os dados com `Map<Item, ItemStack>` e escreva os métodos de adicionar, consumir e contar de acordo com as regras de chaves do mapa.
2. **Montar Contratos de Venda:** Crie a assinatura em `Seller` para trabalhar com a coleção de `ItemStack`.
3. **Escrever o `ShopService`:** Implemente as checagens matemáticas puras de saldo, manipulação do inventário do jogador e transferência de valores com base no preço base do item.

### Referências

* `diagrams/03-items-inventory-capture.puml`