(ns dec06
  (require '[clojure.string :as str])
  (require '[clojure.set :as set])
  (require '[clojure.test :as tst])

  (def testdata (slurp "test-input1.txt")) ;;First test data
  (def taskinput (slurp "input.txt")) ;;Task input

  ;; Part I
  ;; 
  (defn strip-whitespace [s]
    (str/replace s #"\s" ""))

  (defn dec06-1 [thisinput]
    (reduce
     +
     (map
      count
      (map
       frequencies
       (map strip-whitespace
            (str/split thisinput #"\n\n"))))))

  (tst/is (= (dec06-1 testdata) 11))
  (tst/is (= (dec06-1 taskinput) 6587))

  ;; Part II

  (defn forall-in-group [group]
    (apply
     set/intersection
     (vec
      (map
       set
       (map
        keys
        (map frequencies
             (str/split-lines group)))))))

  (defn dec06-2 [thisinput]
    (reduce
     +
     (map
      count
      (vec
       (map forall-in-group
            (vec (str/split thisinput #"\n\n")))))))

  (tst/is (= (dec06-2 testdata) 6))
  (tst/is (= (dec06-2 taskinput) 3235))

  (time (dec06-2 taskinput))
  
  )
