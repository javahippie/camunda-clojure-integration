(ns engine-clojure.core
    (:gen-class)
    (:require [compojure.core :refer :all]
              [org.httpkit.server :refer [run-server]]
              [cheshire.core :as json]
              [engine-clojure.camunda :as c]))

(defn to-string [payload]
  (json/generate-string payload))

(defn from-string [json]
  (json/parse-string json true))

(defroutes myapp
  (GET "/task/:id" [id]
       (to-string (c/get-task id)))

  (POST "/task/:id/claim" {body :body}
        (let [payload (from-string (slurp body))
              id (:id payload)
              user (:user payload)]
          (c/claim-task id user)))

  (POST "/task/:id/complete" request
        (print (:params request)))
        
  (GET "/tasks" []
       (to-string (c/get-tasks)))
        
  (GET "/tasks/:candidategroup" [candidategroup]
       (to-string (c/get-tasks-for-candidate-group candidategroup)))
        
  (POST "/start" []
        (.getId (c/start-instance-by-key "complaint-process"
                                         {"value" 500}))))

(defn -main []
  (c/bootstrap)
  (run-server myapp {:port 5000}))
