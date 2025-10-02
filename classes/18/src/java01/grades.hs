students = [("Alice",1001,8.55),("Bob", 1002,7.8),("Charlie", 1003,9.2)]

-- Warning: this does not test for zero-division
average = sum grades / fromIntegral (length grades)
  where
    grades = [grade | (_,_,grade) <- students]

-- Alternative: a function
meanGrade xs
  | null xs   = 0.0
  | otherwise = sum (map (\(_,_,g) -> g) xs) / fromIntegral (length xs)