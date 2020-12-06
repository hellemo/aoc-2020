(require '[clojure.string :as str])

(defn pwd [l] (get (str/split l #": ") 1))
(defn rule [l] (get (str/split l #": ") 0))

(defn count-chars [s char]
  (- (count (str/replace s char "--"))
     (count s)))

(defn num-in-range [range num]
  (and (>= num (Integer/parseInt (get (str/split range #"-") 0)))
       (<= num (Integer/parseInt (get (str/split range #"-") 1)))))

(defn rule-char [rule]
  (get (str/split rule #" ") 1))

(defn rule-range [rule]
  (get (str/split rule #" ") 0))

(defn follows-rule [pwd rule]
  (num-in-range (rule-range rule) (count-chars pwd (rule-char rule))))

(defn check-line [l]
  (follows-rule (pwd l) (rule l)))

(defn check-list [l]
  (get (frequencies (map check-line (str/split-lines l))) true))

(def testdata (slurp "../aoc-dec02/input.txt"))

; Part I
(require '[clojure.test :as tst])
(tst/is (= (check-list testdata) 586))

; Part II
(defn char-at-pos [s c pos]
  (if (<= pos (count s))
    (= (subs s (dec pos) pos) c)
    false))

(defn rule-pos [rule pos]
  (Integer/parseInt (get (str/split (rule-range rule) #"-") pos)))

(defn follows-rule [pwd rule]
  (= (get (frequencies
           (for [p [0 1]] (char-at-pos pwd (rule-char rule) (rule-pos rule p)))) true) 1))

(tst/is (= (check-list testdata) 352))

