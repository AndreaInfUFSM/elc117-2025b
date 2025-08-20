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

