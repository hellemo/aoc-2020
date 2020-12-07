(ns aoc-dec07)

(require '[clojure.string :as str])
(require '[clojure.test :as tst])
(use 'clojure.set)

;; Input data
(def td1 (slurp "testinput1.txt"))
(def td2 (slurp "testinput2.txt"))
(def testdata (slurp "input.txt"))

;; Part I
(defn bag-type [somebag]
  (str/replace (str/replace (str/trim (str/replace (str/replace (str/trim somebag) "s." "") #"\s?\d+\s?" "")) "bags" "bag") "bag." "bag"))

(defn contains-bag [inspec]
  (let [[container content] (str/split inspec #"contain")]
    (zipmap (map bag-type (str/split content #","))  (take (count content) (repeat (bag-type container))))))

(defn all-bagtypes [baglist]
  (union
   (set (flatten (map keys (map contains-bag baglist))))
   (set (flatten (map vals (map contains-bag baglist))))))

(defn contains-directly [baglist bag]
  (remove nil? (set (map (fn [bl] (get bl bag)) (vec (merge-with union (map contains-bag baglist)))))))

(defn contains-nested [baglist bag]
  (def directly (contains-directly baglist bag))
  (if (> (count directly) 0)
    (def indirectly (flatten [[] (map (fn [b] (contains-nested baglist b)) directly)]))
    directly)
  (flatten [directly indirectly]))

(defn dec07-1 [testdata bag]
  (def baglist (str/split-lines testdata))
  (count (set (flatten (contains-nested baglist bag)))))

(tst/is (= (dec07-1 td1 "shiny gold bag") 4))
(tst/is (= (dec07-1 testdata "shiny gold bag") 300))

;; Part II
(defn bag-num-type [somebag]
  (str/replace (str/replace (str/trim (str/replace (str/replace (str/trim somebag) "s." "") #"^\s?" "")) "bags" "bag") "bag." "bag"))

(defn contains-num-bag [inspec]
  (let [[container content] (str/split inspec #"contain")]
    (zipmap [(bag-type container)] [(set (map bag-num-type (str/split content #",")))])))


(defn num-bag [numbag]
  (if (re-matches #"^\d+\s.*" numbag)
    (Integer/parseInt (first (str/split numbag #"\s")))
    0))

; Test subfunctions
(tst/is (= (num-bag "no red green bags") 0))
(tst/is (= (num-bag "1 red green bags") 1))
(tst/is (= (bag-type "1 red green bags") 1))

(defn contains-num-directly [baglist bag]
  (remove nil? (map (fn [bl] (get bl bag)) (vec (merge-with union (map contains-num-bag baglist))))))

(defn contains-list [baglist]
  (def ks (all-bagtypes baglist))
  (def vs (map (fn [b] (contains-num-directly baglist b)) (all-bagtypes baglist)))
  (zipmap ks vs))

(defn count-bags [numbags]
  (reduce + (map num-bag (first (seq numbags)))))

(defn traverse-bags [cl bag]
  (def numbags (get cl bag))
  (if (> (count-bags numbags) 0)
    (reduce + (count-bags numbags)
            (map (fn [b] (* (num-bag b)
                            (traverse-bags cl (bag-type b))))
                 (first (seq numbags))))
    0))

(defn dec07-2 [testdata2 bag2]
  (def baglist2 (str/split-lines testdata2))
  (def cl2 (contains-list baglist2))
  (def firstlevel (count-bags (get cl2 bag2)))
  (traverse-bags cl2 bag2))


;; Test results
(tst/is (= (dec07-2 td2 "shiny gold bag") 126))
(tst/is (= (dec07-2 td2 "dark green bag") 6))
(tst/is (= (dec07-2 td2 "dark blue bag") 2))
(tst/is (= (dec07-2 td2 "dark violet bag") 0))

(tst/is (= (dec07-2 testdata "shiny gold bag") 8030))
