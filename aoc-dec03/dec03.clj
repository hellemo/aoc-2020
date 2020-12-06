(require '[clojure.string :as str])

(defn is-tree [thismap x y] ;1-indexed positions x and y
    (def splitmap (str/split-lines thismap))
    (def Y (count splitmap))
    (def X (count (get splitmap 0)))
    (if
    (= (get (get splitmap (dec y)) (mod (dec x) X)) (get "#" 0))
    1
    0
    )
)

(defn count-path [thismap [x y]]
    (def Y (count (str/split-lines thismap)))
    (def has-tree (is-tree thismap x y))
    (println x y has-tree)
    (if (< y Y)
        (+ has-tree (count-path thismap (next-pos [x y])))
        (is-tree thismap x y);has-tree
    )
)

(defn next-pos
  [[x y] [dx dy]]
  [(+ x dx) (+ y dy)]
  )

(defn count-path [thismap [x y] next_pos]
    (def Y (count (str/split-lines thismap)))
    (def has-tree (is-tree thismap x y))
    ; (println x y has-tree)
    (if (< y Y)
        (+ has-tree (count-path thismap (next_pos [x y]) next_pos))
        (is-tree thismap x y);has-tree
    )
)

(defn dec03-1 [thismap]
  (count-path thismap [1 1] (fn [[x y]] (next-pos [x y] [3 1])))
  )


(defn dec03-2 [thismap]
    (def slopes [[1 1] [3 1] [5 1] [7 1] [1 2]])
    (reduce * (for [s slopes] (count-path thismap [1 1] (fn [[x y]] (next-pos [x y] s)))))
)

(def inputdata (slurp "input.txt"))

