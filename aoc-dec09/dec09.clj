(ns dec09)

(require '[clojure.string :as str])
(require '[clojure.test :as tst])

(def td1 (slurp "testinput1.txt"))

(defn to-int [s]
  (bigint s))

(defn to-integers [ms]
  (map to-int (str/split-lines ms)))

(defn is-valid [li n]
  (first (for [i li j li :when (= (+ i j) n)] n)))

(defn is-invalid [li n]
  (if (is-valid li n)
    nil
    n))

(tst/is (= (is-invalid '(1 2 3 4 5) 100) 100))

(defn find-invalid [li pre]
  (if (< pre (count li))
    (let [myval (is-invalid (take pre li)
                            (nth li pre))]
      (if (nil? myval)
        (find-invalid (rest li) pre)
        myval))
    nil))

(tst/is (= (find-invalid (to-integers td1) 5) 127))
(def find-invalid-memo (memoize find-invalid))

(defn dec09-1 [input n]
  (let [li (to-integers input)]
    (find-invalid-memo li n)))

(def testdata (slurp "input.txt"))
(tst/is (= (dec09-1 td1 5) 127))
(tst/is (= (dec09-1 testdata 25) 23278925))

(defn valid-seq [li n]
  (= (reduce + li) n))
(def valid-seq-memo (memoize valid-seq))

(tst/is (valid-seq '(1 1 1) 3))
(tst/is (not (valid-seq '(1 1 1) 2)))

(defn find-valid-seq-n [li mn n]
  (if (> n (count li))
    nil
    (if (valid-seq-memo (take n li) mn)
      (take n li)
      (find-valid-seq-n (rest li) mn n))))

(tst/is (= (find-valid-seq-n '(1 1 1) 3 3) '(1 1 1)))
(tst/is (nil? (find-valid-seq-n '(1 1 1) 3 2)))
(tst/is (= (find-valid-seq-n '(2 3 4) 5 2) '(2 3)))

(defn find-valid-seq [li mn n]
  (if (> n (count li))
    nil
    (let [validn (find-valid-seq-n li mn n)]
      (if (nil? validn)
        (let [validnplus (find-valid-seq li mn (inc n))
              validnotnil (first (reverse (sort [validnplus])))]
          (vec validnotnil))
        (vec validn)))))

(tst/is (= (find-valid-seq '(1 1 2 3) 5 2) [2, 3]))
(tst/is (= (find-valid-seq '(1 2 3 4) 6 3) [1,2,3]))
(tst/is (= (find-valid-seq '(2 3 4) 5 2) [2,3]))
(tst/is (= (find-valid-seq '(2 3) 5 2) [2, 3]))
(tst/is (= (find-valid-seq '(35 20 15 25) 60 2) [20 15 25]))

(defn compute-res [li mn]
  (let [tmp (sort (find-valid-seq (filter #(< % mn) li) mn 2))]
    (+ (first tmp) (last tmp))))

(tst/is (= (compute-res '(1 1 2 3) 5) 5))
(tst/is (= (compute-res '(1 1 2 3 4) 5) 5))
(tst/is (= (compute-res (take 5 (to-integers td1)) 35) 35))
(tst/is (= (find-valid-seq (to-integers td1) 127 2) [15N 25N 47N 40N]))
(tst/is (= (find-valid-seq (take 40 (to-integers testdata)) 50 2) [38N 4N 8N]))

(defn dec09-2 [input pre]
  (let [li (to-integers input)
        magicnumber (dec09-1 input pre)]
    (compute-res li magicnumber)))

(tst/is (= (dec09-2 td1 5) 62))
(tst/is (= (dec09-2 testdata 25) 4011064))

(time (dec09-2 testdata 25))
