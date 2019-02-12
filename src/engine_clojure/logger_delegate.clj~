(ns engine-clojure.logger-delegate
  (:gen-class
   :name de.javahippie.camunda.LoggerDelegate
   :implements [org.camunda.bpm.engine.delegate.JavaDelegate]))

(defn -execute [this execution]
  (print (.getVariables execution)))

 


 
