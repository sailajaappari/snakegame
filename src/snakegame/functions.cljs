(ns snakegame.functions)

;; 38(up), 40(down), 37(left), 39(right)
(def key-code->move {38 [0 -1]
                     40 [0 1]
                     39 [1 0]
                     37 [-1 0]})

(defn rand-free-position
  "This function takes the snake and the board-size as arguments,
  and returns a random position not colliding with the snake body"
  [snake [x y]]
  (let [snake-positions-set (into #{} (:body snake))
        board-positions (for [x-pos (range x)
                              y-pos (range y)]
                          [x-pos y-pos])]
    (when-let [free-positions (seq (remove snake-positions-set board-positions))]
      (rand-nth free-positions))))


(defn move-snake
  [{:keys [direction body] :as snake}]
  (let [head-new-position (mapv + direction (first body))]
    (update-in snake [:body] #(into [] (drop-last (cons head-new-position body))))))

(defn change-snake-direction
  "Changes the snake head direction, only when it's perpendicular to the old head direction"
  [[new-x new-y] [x y]]
  (if (or (= x new-x) (= y new-y))
    [x y]
    [new-x new-y]))

(defn snake-tail [coordinate-1 coordinate-2]
  (if (= coordinate-1 coordinate-2)
    coordinate-1
    (if (> coordinate-1 coordinate-2)
      (dec coordinate-2)
      (inc coordinate-2))))

(defn grow-snake
  "Append a new tail body segment to the snake"
  [{:keys [body direction] :as snake}]
  (let [[[first-x first-y] [second-x second-y]] (take-last 2 body)
        x (snake-tail first-x second-x)
        y (snake-tail first-y second-y)]
     (update-in snake [:body] #(conj % [x y]))))

(defn process-move
  [{:keys [snake point board] :as db}]
  (if (= point (first (:body snake)))
    (-> db
        (update-in [:snake] grow-snake)
        (update-in [:points] inc)
        (assoc :point (rand-free-position snake board)))
    db))


(defn collisions
  [snake board]
  (let [{:keys [body direction]} snake
        [x y] board
        boarder-x #{x -1}
        boarder-y #{y -1}
        future-x (+ (first direction) (ffirst body))
        future-y (+ (second diretcion) (second (first body)))]
     (or (contains? boarder-x future-x)
         (contains? boarder-y future-y)
         (contains? (into #{} (rest body)) [future-x future-y]))))
