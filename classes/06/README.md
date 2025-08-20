<!--
author:   Andrea Charão

email:    andrea@inf.ufsm.br

version:  0.0.1

language: PT-BR

narrator: Brazilian Portuguese Female

comment:  Material de apoio para a disciplina
          ELC117 - Paradigmas de Programação
          da Universidade Federal de Santa Maria

translation: English  translations/English.md

link:     https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.css

script:   https://cdn.jsdelivr.net/chartist.js/latest/chartist.min.js



-->

<!--
nvm use v14.21.1
liascript-devserver --input README.md --port 3001 --live
https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/master/classes/06/README.md
-->

[![LiaScript](https://raw.githubusercontent.com/LiaScript/LiaScript/master/badges/course.svg)](https://liascript.github.io/course/?https://raw.githubusercontent.com/AndreaInfUFSM/elc117-2025b/main/classes/06/README.md)


# Programação Funcional em Haskell

> Teste o que você entendeu até agora!


## Quiz

**ATENÇÃO!!!**

> Este quiz não tem correção automática e deve preferencialmente ser feito em aula, junto com toda a turma! 

> **Aguarde instruções da professora para se conectar com a turma**.




### Questão 1



A função `isAlpha :: Char -> Bool` é pré-definida e acessível via `import Data.Char`.

Esta função **recebe um único caracter** e retorna `True` se o caracter for alfabético (minúsculo, maiúsculo ou acentuado), ou `False` em caso contrário (pontuação, símbolos, espaços, dígitos).

Sabendo disso, qual será o resultado de `map isAlpha "abcd"`.


- [(a)] a) `True`
- [(b)] b) `[True,True,True,True]`
- [(c)] c) `"ABCD"`



### Questão 2

Considerando a função `isAlpha :: Char -> Bool` da questão anterior, qual uso dessa função vai gerar um erro?

- [(a)] a) `filter isAlpha "a b c"`
- [(b)] b) `map (\c -> if isAlpha c then c else ' ') "a b c"`
- [(c)] c) `isAlpha "a b c"`


### Questão 3

Qual das funções abaixo poderia substituir a função lambda em

```haskell
filter (\c -> isAlpha c || c == ' ') "a-bra ca-da-bra"
```

(a)

```haskell 
func :: Char -> Bool
func x = x == ' ' || isAlpha x
```

(b)

```haskell 
func :: String -> Bool
func c = isAlpha c || c == ' '
```

- [(a)] (a)
- [(b)] (b)



### Questão 4

Considere que:

A função `replicate :: Int -> a -> [a]` recebe um número inteiro e um valor de qualquer tipo. Essa função repete o valor um certo número de vezes, formando uma lista.

```
ghci> replicate 5 'a'
"aaaaa"
```

A função `min :: Ord a => a -> a -> a` recebe dois valores comparáveis e retorna o menor deles (`Ord` é uma classe de tipos, de forma que a função se aplica a qualquer tipo que seja da classe).

```
ghci> min 8 9
8
ghci> min "abc" "de"
"abc"
```

A função `pad :: Int -> String -> String` é definida como:

```haskell
pad :: Int -> String -> String
pad n s = replicate (min n (n - length s)) '-' ++ s
```

Qual será o resultado de `pad 5 "ola"`?:

- [(a)] `"ola"`
- [(b)] `"--ola"`
- [(c)] `"ola--"`


### Questão 5

O código a seguir usa **tuplas** e **lambda**. Qual será o resultado de 

```haskell
map (\(x,y) -> x + y) [(1,2),(3,4)]
```


- [(a)] `[1,2,3,4]`
- [(b)] `10`
- [(c)] `[3,7]`





### Questão 6

Considere a seguinte definição de função:

```haskell
func :: [(String, Int)] -> [String]
func ss = map (\(w, n) -> replicate (min 10 n) '#' ++ " " ++ w) ss
```

Qual será o resultado de `func [("Java", 3),("Haskell",5)]`?

- [(a)] `["### Java","##### Haskell"]`
- [(b)] `["##########","Java","Haskell"]`
- [(c)] `["Java ###","Haskell #####"]`



### Questão 7


Haskell tem um recurso de linguagem chamado "pattern matching", que pode ser usado para definir funções de acordo com o padrão do argumento, sem necessidade de if/else explícito.

```haskell
describeNumber :: Int -> String
describeNumber 0 = "zero"
describeNumber 1 = "um"
describeNumber 2 = "dois"
describeNumber _ = "outro número"
```

Qual será o resultado de:

```haskell
length (describeNumber 10)
```
[[12]]


### Questão 8

Note que é possível usar pattern matching com tuplas e listas: 

```haskell
quadrante :: (Double, Double) -> String
quadrante (x, y)
  | x > 0 && y > 0 = "Primeiro quadrante"
  | x < 0 && y > 0 = "Segundo quadrante"
  | x < 0 && y < 0 = "Terceiro quadrante"
  | x > 0 && y < 0 = "Quarto quadrante"
  | otherwise      = "Na origem ou em um eixo"
```

```haskell
listInfo :: [Int] -> String
listInfo []        = "Lista vazia"
listInfo [x]       = "Lista com um elemento"
listInfo [x, y]    = "Lista com dois elementos"
listInfo (x:y:_)   = "Lista com três ou mais elementos"
```


Qual será o resultado de:

```haskell
length (filter (== ' ') (quadrante (1,2) ++ listInfo [1,2,3]))
```
[[6]]







## Um programa completo


- Análise de respostas da turma a uma pergunta deste [questionário](https://forms.gle/9XQ8GQ8Y3JVTAaKk6) (ver aula 1): "Em quais linguagens você já programou?"
- Texto das respostas foi pré-processado, mantendo apenas as linguagens citadas (na forma em que foram escritas)
- Dados: arquivo [conhecidas.txt](src/conhecidas.txt)


> Problema: como gerar um "ranking" das linguagens já usadas pela turma?

### Decomposição

- Passo 1: separar em tokens, sem pontuação
- Passo 2: normalizar tudo em minúsculas
- Passo 3: contar palavras, sem repetições
- Passo 4: ordenar pela frequência (da maior para a menor)
- Passo 5: gerar representação do ranking


### Solução

Programa [ranking.hs](src/ranking.hs):

```haskell
import System.Environment (getArgs, getProgName)
import Data.Char (isAlpha, toLower)
import Data.List (sortOn)
import qualified Data.Map as Map

-- Passo 1: separar em tokens, sem pontuação
tokenize :: String -> [String]
tokenize str = words (map (\c -> if isAlpha c then c else ' ') str)

-- Passo 2: normalizar tudo em minúsculas
normalize :: [String] -> [String]
normalize ss = map (map toLower) ss

-- Passo 3: contar palavras, sem repetições
countWords :: [String] -> Map.Map String Int
countWords ws = Map.fromListWith (+) (map (\w -> (w,1)) ws)

-- Passo 4: ordenar pela frequência (da maior para a menor)
sortCounts :: Map.Map String Int -> [(String, Int)]
sortCounts m = sortOn (\(_,n) -> negate n) (Map.toList m)

-- Passo 5: gerar tabela
renderTable :: [(String, Int)] -> String
renderTable xs = unlines (map (\(w, n) -> w ++ "\t" ++ show n) xs)
-- Outras formas equivalentes:
-- renderTable xs = unlines $ map (\(w, n) -> w ++ "\t" ++ show n) xs
-- renderTable = unlines . map (\(w, n) -> w ++ "\t" ++ show n) 
-- renderTable xs = (unlines . map (\(w, n) -> w ++ "\t" ++ show n)) xs

-- Alternativa passo 5: gerar gráfico de barras com caracteres
renderRanking :: [(String, Int)] -> String
renderRanking xs = unlines (map (\(w, n) -> replicate (min 50 n) '#' ++ " " ++ w) xs)

-- Passo 6: Pipeline completo
wordFrequencyPipeline :: String -> String
wordFrequencyPipeline =
    renderRanking
    . sortCounts
    . countWords
    . normalize
    . tokenize

-- Main: IO isolada das funções "puras"
main :: IO ()
main = do
    progName <- getProgName
    args <- getArgs
    case args of
        [filename] -> do
            text <- readFile filename
            putStrLn (wordFrequencyPipeline text)
        _ -> putStrLn $ "Usage: runhaskell " ++ progName ++ " <filename>"
```

### Questões

1. Como executar o programa?
2. Quais as funções já conhecidas? Quais as desconhecidas?
3. Formas equivalentes: o que significam `$` e `.` ?
