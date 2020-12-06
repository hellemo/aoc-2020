(require '[clojure.string :as str])
(require '[clojure.spec.alpha :as s])

(defn entries-from-text [input]
    (str/split input #"\s\n");
)
(defn entry-keys [input]
    (def entries (str/split input #"\s"))
    ; (println entries)
    ; (def )
    (def kv (map str/split (str/split entries #"\s") #":"))
    ; (get kv 0)
    ; entries
    kv
)

(defn key-from-entry [entry]
    (keyword(get(str/split entry #":")0))
)

(defn key-from-pair [pair]
    (keyword(get(str/split pair #":")0))
)

(defn val-from-pair [pair]
    (get(str/split pair #":")1)
)

(defn dict-entry [entry]
    (def tk (map key-from-pair (str/split entry #"\s")))
    (def tv (map val-from-pair (str/split entry #"\s")))
    (zipmap tk tv)
)

(defn is-valid [pstring pspec ]
    (if 
        (= :clojure.spec.alpha/invalid (s/conform pspec (dict-entry pstring)))
        0
        1
    )
)

(defn valid-passport [pstring]
    (is-valid pstring ::passport)
)

(defn pair-from-entry [entry]
    (def entry_pair (str/split entry #":"))
    (keyword(get entry_pair 0)) (get entry_pair 1)
)

(defn pair-from entries [entries]
    (map pair-from-entry entries)
)


 (s/def ::hgt string?)
 ;(s/def ::hgt (s/and string? #(re-matches h-regex %)))
 (s/def ::ecl string?)
 (s/def ::pid string?)
 (s/def ::eyr string?)
 (s/def ::hcl string?)
 (s/def ::byr string?)
 (s/def ::iyr string?)
 (s/def ::cid string?)

(s/def ::passport (s/keys :req-un [::hgt ::ecl ::pid ::eyr ::hcl ::byr ::iyr]
                          :opt-un [::cid]))


(defn s-int-between [s-int lo hi]
    (and (>= (Integer/parseInt s-int) lo)
         (<= (Integer/parseInt s-int) hi)
    )
)

(defn s-int-between-strip [s-int lo hi rm]
    (s-int-between (str/replace s-int rm "") lo hi)
)

;TODO: Namespaces
; hgt (Height) - a number followed by either cm or in:

;     If cm, the number must be at least 150 and at most 193.
;     If in, the number must be at least
(s/def ::hgt-in (s/and string?
                    #(re-matches #"\d*in" %)
                    #(s-int-between-strip % 59 76 "in")
                )
)
(s/def ::hgt-cm (s/and string?
                    #(re-matches #"\d*cm" %)
                    #(s-int-between-strip % 150 193 "cm")
                )
)
(s/def ::hgt (s/or :in ::hgt-in :cm ::hgt-cm))
; ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
 (s/def ::ecl (s/and string? 
                    #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}
 ))
;  pid (Passport ID) - a nine-digit number, including leading zeroes.
 (s/def ::pid (s/and string? 
                    #(re-matches #"\d{9}" %)
 ))
 ;eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
 (s/def ::eyr (s/and string? 
                    #(re-matches #"\d{4}" %)
                    #(s-int-between % 2020 2030)
                    ))
;  hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f
 (s/def ::hcl (s/and string?
                    #(re-matches #"\#[0-9a-f]{6}" %)
 ) )
 ;byr (Birth Year) - four digits; at least 1920 and at most 2002.
 (s/def ::byr (s/and string? 
                    #(re-matches #"\d{4}" %)
                    #(s-int-between % 1920 2002)
                    ))
 ; iyr (Issue Year) - four digits; at least 2010 and at most 2020.
 (s/def ::iyr (s/and string? 
                    #(re-matches #"\d{4}" %)
                    #(s-int-between % 2010 2020)
                    ))
 (s/def ::cid string?)

(s/def ::passport-s (s/keys :req-un [::hgt ::ecl ::pid ::eyr ::hcl ::byr ::iyr]
                          :opt-un [::cid]))



(defn dec04_1 [input]
    (reduce + (map valid-passport (entries-from-text input)))
)

(defn valid-passport-strict [opstring]
    (if 
        (= :clojure.spec.alpha/invalid (s/conform ::passport-s (dict-entry opstring)))
        0
        1
    )
)

(defn dec04_2 [d2in]
    (reduce + (map valid-passport-strict (entries-from-text d2in)))
    ; (map valid-passport-strict (entries-from-text input))
    ; (entries-from-text d2in)
)
