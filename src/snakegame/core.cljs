(ns snakegame.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-handler register-sub subscribe dispatch dispatch-sync]]
            [goog.events :as events]
            [snakegame.handlers :as handlers]
            [snakegame.view :as view]))

(enable-console-print!)

(defn game []
  [:div])

(defn run [] 
  (dispatch-sync [:initialize])
  (reagent/render [game] (js/document.getElementById "app")))

(run)
