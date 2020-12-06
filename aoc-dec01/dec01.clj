(def x [1721
        979
        366
        299
        675
        1456])

(take 1 (for [i  x j x :when (= (+ i j) 2020)] (* i j)))

(require '[clojure.string :as str])

(def testdata (slurp "../aoc-dec01/input.txt"))

(defn dec01-1 [inp]
  (def x (str/split-lines inp))
  (take 1 (for [i x j x :when (= (+ i j) 2020)] (* i j))))

(dec01-1 testdata)

(defn dec01-2 [inp]
  (def x (str/split-lines inp))
  (take 1 (for [i x j x k x :when (= (+ i j k) 2020)] (* i j k))))

(dec01-2 testdata)

(str/split-lines testdata)