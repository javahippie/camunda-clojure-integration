(def project 'engine-clojure)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
			    [org.camunda.bpm/camunda-engine "7.10.0"]
                 	    [com.h2database/h2 "1.4.197"]
                            [adzerk/boot-test "RELEASE" :scope "test"]
                            [compojure "1.1.8"]
                            [http-kit "2.1.16"]
                            [cheshire "5.8.1"]
                            [clojure-jsr-223 "0.3.0"]
			    [org.slf4j/slf4j-simple "1.7.25"]])

(task-options!
 aot {:namespace   #{'engine-clojure.core}}
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/javahippie/camunda-clojure-integration"}
      :license     {"Apache License 2.0"
                    "http://www.apache.org/licenses/"}}
 repl {:init-ns    'engine-clojure.core}
 jar {:main        'engine-clojure.core
      :file        (str "engine-clojure-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (with-pass-thru fs
    (require '[engine-clojure.core :as app])
    (apply (resolve 'app/-main) args)))

(require '[adzerk.boot-test :refer [test]])
