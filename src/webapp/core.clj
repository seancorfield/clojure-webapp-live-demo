(ns webapp.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [com.stuartsierra.component :as component]
            [ring.adapter.jetty :as jetty]))

(defroutes app-routes)

(defroutes base-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defrecord WebServer [http-server shutdown]
  component/Lifecycle
  (start [this]
         (if http-server
           this
           (assoc this
                  :http-server (jetty/run-jetty (routes #'app-routes #'base-routes)
                                                {:port 7777 :join? false})
                  :shutdown (promise))))
  (stop [this]
        (if http-server
          (do
            (.stop http-server)
            (deliver shutdown true)
            (assoc this :http-server nil :shutdown nil))
          this)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [system (map->WebServer {})]
    (-> (component/start system) :shutdown deref)))

(comment
 (def system (map->WebServer {}))
 (alter-var-root #'system component/start)
 (alter-var-root #'system component/stop))
