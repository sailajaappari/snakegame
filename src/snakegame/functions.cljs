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
