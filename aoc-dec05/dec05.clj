(require '[clojure.string :as str])

(defn row-from-string [st]
  (Integer/parseInt (str/replace (str/replace st "B" "1") "F" "0") 2))

(defn col-from-string [st]
  (Integer/parseInt (str/replace (str/replace st "R" "1") "L" "0") 2))

(defn seat [st]
  (def r (row-from-string (subs st 0 7)))
  (def c (col-from-string (subs st 7 10)))
  (+ (* r 8) c))

(row-from-string "BBB")
(col-from-string "RLL")

(Integer/parseInt (str/replace (str/replace "BBFBFBF" "B" "0") "F" "1") 2)

(require '[clojure.test :as tst])

(def t1 "BFFFBBFRRR")
(def t2 "FFFBBBFRRR")
(def t3 "BBFFBBFRLL")
(tst/is (= (seat t1) 567))
(tst/is (= (seat t2) 119))
(tst/is (= (seat t3) 820))

(defn dec05-1 [st]
  (apply max (map seat (str/split-lines st))))

(defn dec05-2 [st]
  (compare-next (sort (map seat (str/split-lines st)))))

(defn compare-next [li]
  (if (< (apply - (take 2 li)) -1) ;; Difference > 1 means match
    (inc (first li)) ;; Matching number is missing (between first two in li) 
    (compare-next (next li)))) ;; Else continue searching

(def inputdata (slurp "input.txt"))

(time (tst/is (= (dec05-1 inputdata) 842)))
(time (tst/is (= (dec05-2 inputdata) 617)))