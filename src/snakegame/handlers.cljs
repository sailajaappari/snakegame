(ns snakegame.handlers
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [snakegame.functions :as f :refer [rand-free-position move-snake change-snake-direction key-code->move process-move]]
            [re-frame.core :refer [register-handler register-sub subscribe dispatch dispatch-sync]]
            [goog.events :as events]))

(def board [35 25])

(def snake {:direction [1 0]
            :body [[3 2] [2 2] [1 2] [0 2]]})

(def initial-state {:board board
                    :snake snake
                    :point (rand-free-position snake board)
                    :points 0
                    :game-running? true})
;; Handlers
(register-handler                  
 :initialize                       
 (fn
   [db _]                         
   (merge db initial-state)))    

(register-handler
  :next-state
  (fn [{:keys [snake board] :as db} _]
    (if (:game-running? db)
      (if (f/collisions snake board)
          (assoc-in db [:game-running?] false)
          (-> db
              (update-in [:snake] move-snake)
              (as-> after-move
                    (process-move after-move))))
      db)))

(register-handler
  :change-direction
  (fn [db [_ new-direction]]
    (update-in db [:snake :direction] 
              (partial change-snake-direction new-direction))))

(defonce key-handler
  (events/listen js/window "keydown"
                 (fn [e]
                   (let [key-code (.-keyCode e)]
                      (when (contains? key-code->move key-code)
                         (dispatch [:change-direction (key-code->move key-code)]))))))



;; Subscribers
(register-sub
  :board
  (fn
    [db _]
    (reaction (:board @db))))  

(register-sub
  :snake
  (fn [db _]
    (reaction (:body (:snake @db)))))

(register-sub
  :point
  (fn [db _]
    (reaction (:point @db))))

(register-sub
  :points
  (fn [db _]
    (reaction (:points @db))))

(register-sub
  :game-running?
  (fn [db _]
    (reaction (:game-running? @db))))

