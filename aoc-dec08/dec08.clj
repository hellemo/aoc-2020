(ns dec08)

(require '[clojure.string :as str])
(require '[clojure.test :as tst])

(def td1 (slurp "testinput1.txt"))
(def testdata (slurp "input.txt"))

(defn next-pos [pos instr val]
  (if (= instr "jmp")
    (+ pos (Integer/parseInt val))
    (inc pos)))

(defn next-val [accumulator instr val]
  (if (= instr "acc")
    (+ accumulator (Integer/parseInt val))
    accumulator))

(defn execute-instruction [instructions pos accumulator notvisited]
  (let [instrval (str/split (get instructions pos) #"\s")
        instr (get instrval 0)
        val (get instrval 1)]
    (if (get notvisited pos)
      (execute-instruction
       instructions
       (next-pos pos instr val)
       (next-val accumulator instr val)
       (assoc notvisited pos false))
      accumulator)))

(defn dec08-1 [input]
  (def instructionlist (str/split-lines input))
  (def notvisited (vec (for [i instructionlist] true)))
  (execute-instruction instructionlist 0 0 notvisited))

;; Testing instructions
(= (next-pos 0 "nop" "2") 1)
(= (next-val 0 "nop" "100") 0)
(= (next-val 0 "acc" "100") 100)
(= (next-val 10 "acc" "-100") -90)
(= (next-pos 0 "jmp" "+3") 3)

(tst/is (= (dec08-1 td1)) 4)
(tst/is (= (dec08-1 testdata) 2025))

;; Part II

;; TODO: Lookup table?
(defn flip [instr]
  (if (= instr "acc")
    "acc"
    (if (= instr "jmp")
      "nop"
      "jmp")))

(tst/is (= (flip "acc")) "acc")
(tst/is (= (flip "jmp")) "nop")
(tst/is (= (flip "nop")) "jmp")

(defn fix-execute-instruction [instructions pos accumulator notvisited alreadyflipped]
  (let [instrval (if (< pos (count instructions))
                   (str/split (get instructions pos) #"\s")
                   ["nop" 0])
        instr (get instrval 0)
        val (get instrval 1)
        fei (fn [p flpd]
              (fix-execute-instruction instructions p
                                       (next-val accumulator instr val)
                                       (assoc notvisited pos false) flpd))]
    (if (> pos (dec (count instructions)))
      accumulator
      (if (get notvisited pos)
        (let [flippedorlater (fei (next-pos pos instr val) alreadyflipped)
              flipnow (if (not alreadyflipped)
                        (fei (next-pos pos (flip instr) val) true)
                        nil)]
          (first (reverse (sort [flippedorlater flipnow]))))
        nil))))

(defn dec08-2 [input]
  (def instructionlist (str/split-lines input))
  (def notvisited (vec (for [i instructionlist] true)))
  (fix-execute-instruction instructionlist 0 0 notvisited false))

(tst/is (= (dec08-2 td1)) 8)
(tst/is (= (dec08-2 testdata)) 2001)