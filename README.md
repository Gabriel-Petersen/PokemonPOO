# PokemonPOO

Engine de exemplo em Java para exploração/batalha estilo Pokémon (projeto educacional).

## Visão geral

Código-fonte em `src/main/java` e recursos em `src/main/resources`. A aplicação principal é a classe `execs.Main` que inicia a janela Swing e o loop do jogo.

## Requisitos

- JDK 11+ instalado
- Recomendado: IDE Java (IntelliJ IDEA, Eclipse) para facilitar importação e execução

## Compilar e executar

Recomendado: abrir o projeto na sua IDE e executar a classe `execs.Main`.

CLI (Unix/macOS):

```bash
# do diretório raiz do projeto
mkdir -p out
find src/main/java -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out:src/main/resources execs.Main
```

CLI (Windows PowerShell):

```powershell
# do diretório raiz do projeto
New-Item -ItemType Directory -Force -Path out
Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
java -cp out;src/main/resources execs.Main
```

Observação: os recursos (imagens, arquivos de registro) são lidos a partir de `src/main/resources`, por isso incluímos esse diretório no classpath na execução acima.

## Estrutura do projeto (súmula)

- `src/main/java` — código fonte Java (pacotes do motor, entidades, jogo, execs)
- `src/main/resources` — assets (sprites, tilemaps) e registries
  - [src/main/resources/registry/species.txt](src/main/resources/registry/species.txt)
  - [src/main/resources/registry/moves.txt](src/main/resources/registry/moves.txt)

## Formato dos arquivos de registro

`moves.txt`
- Arquivo CSV comentado. Formatos suportados:
  - Movimentos de dano (PHYSICAL/SPECIAL): `ID,NAME,POWER,ACCURACY,PRIORITY,ELEMENT_TYPE,CATEGORY`
  - Movimentos de status: campos adicionais são usados pelo parser (veja `src/main/resources/registry/moves.txt`).

`species.txt`
- Linha por espécie: `ID,NAME,TYPE1,TYPE2,HP,ATK,DEF,SP_ATK,SP_DEF,SPEED,EVOLUTION_ID,[MOVE_NAME,LEVEL,...]`
- `EVOLUTION_ID` deve ser `-1` quando não houver evolução.
- Os nomes de movimentos referenciados em `species.txt` devem existir exatamente em `moves.txt` (mesma escrita e capitalização). Movimentos ausentes serão ignorados/retirados.

## EncounterTable / raridade

A tabela de encontros usa um valor de `rarity` como peso: quanto maior o número, mais comum o Pokémon se torna. A probabilidade é calculada como:

```
probabilidade = rarity / soma_das_rarities
```

Ou seja, se entradas tiverem rarities `1, 3, 5`, as probabilidades aproximadas serão `11.1%`, `33.3%` e `55.6%` respectivamente. A `Builder.addSpecie` exige `rarity > 0`.
(Implementação: `src/main/java/game/capturing/EncounterTable.java`)

## Como contribuir

- Para adicionar um novo movimento: incluir uma linha em `src/main/resources/registry/moves.txt` seguindo o formato.
- Para adicionar/editar uma espécie: editar `src/main/resources/registry/species.txt`. Antes de referenciar um movimento, adicione-o em `moves.txt`.
- Depois de editar arquivos de resource, reinicie a aplicação para recarregar os registries.

## Observações úteis

- Classe principal: `execs.Main` (ponto de entrada)
- Arquivos de assets (sprites, tilemaps) em `src/main/resources/assets/...` — carregados via `engine.assets.AssetManager`.

---

Se quiser, eu faço: validação automática dos `species.txt` (mapeando linhas removidas), adicionar instruções de debug, ou criar um script de build (Maven/Gradle).