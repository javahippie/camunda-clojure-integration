(ns engine-clojure.delegate
  (:gen-class
   :name de.javahippie.camunda.Delegate
   :implements [org.camunda.bpm.engine.delegate.JavaDelegate]))

(defn -execute [this execution]
  (.setVariables execution {"var1" "A Happy String"
                            "var2" "An even happier String"}))
 


 
