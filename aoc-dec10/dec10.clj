(ns dec10)

(require '[clojure.string :as str])
(require '[clojure.test :as tst])

(def td1 (slurp "testinput1.txt"))

(defn to-int [s]
  (Integer/parseInt s))

(defn to-integers [ms]
  (map to-int (str/split-lines ms)))

(defn max-jolt [li]
  (last (sort li)))

(defn valid-dist [current new]
  (let [curdist (- new current)]
    (if (and (> curdist 0)
             (< curdist 4))
      true false)))

(tst/is (not (valid-dist 11 15)))
(tst/is (valid-dist 1 4))

(defn next-adapter [adapterlist currjolt]
  (if (valid-dist currjolt (first adapterlist))
    (first adapterlist)
    nil))

(tst/is (=(next-adapter [1 2 3 4 5] 0) 1))

(defn next-jolt [adapterlist currjolt]
  (if (valid-dist currjolt (first adapterlist))
    (- (first adapterlist) currjolt)
    nil))

(tst/is (= (next-jolt [1 2 3 4 5] 0) 1))

(defn update-distances [distances dist]
  (if (and
       (> dist 0)
       (< dist (count distances)))
    (assoc distances dist
           (inc (get distances dist)))
    distances))

(tst/is (=(update-distances [0 1 0 0] 1) [0 2 0 0]))

(defn check-adapters  [adapterlist currjolt maxjolt currdistances]
  (if (= (count adapterlist) 0)
    currdistances
    (let [thisadapter (first adapterlist)
          thisdist (- thisadapter currjolt)
          newjolt (+ currjolt thisdist)
          newdistances  (update-distances currdistances thisdist)]
      (def ca (fn [jolt distances]
                (check-adapters
                 (rest adapterlist)
                 jolt
                 maxjolt
                 distances)))
      (if (valid-dist currjolt thisadapter)
        (ca newjolt newdistances)
        (ca currjolt currdistances)))))

(defn dec10-1 [data]
  (let [distances (update-distances (check-adapters (sort (to-integers data)) 0 19 [0 0 0 0]) 3)]
    (* (get distances 1) (get distances 3))))

(tst/is (=(dec10-1 td1) 35))

(def td2 (slurp "testinput2.txt"))
(def testdata (slurp "input.txt"))

(tst/is (= (dec10-1 td2) 220))

(tst/is (= (dec10-1 testdata) 2310))

