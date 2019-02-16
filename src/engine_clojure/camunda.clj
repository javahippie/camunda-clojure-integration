(ns engine-clojure.camunda)

(defn build-engine []
  (Class/forName "org.h2.Driver")
  (let [camunda (org.camunda.bpm.engine.ProcessEngineConfiguration/createStandaloneInMemProcessEngineConfiguration)]
    (.buildProcessEngine camunda)))

(def camunda (build-engine))

(defn user-service []
  (.getIdentityService camunda))

(defn deploy-process [definition-file]
  (let [repo-service (.getRepositoryService camunda)
        name         "process.bpmn"
        input-stream (clojure.java.io/input-stream definition-file)]
    (.newUser (user-service) "fred")
    (.newGroup (user-service) "specialists")
    (.deploy (.addInputStream (.createDeployment repo-service)            
                              name
                              input-stream))))

(defn start-instance-by-key [key variables]
  (.startProcessInstanceByKey (.getRuntimeService camunda) key variables))

(defn bootstrap []
  (deploy-process "resources/Complaint.bpmn"))

(defn task-service []
  (.getTaskService camunda))

(defn runtime-service []
  (.getRuntimeService camunda))

(defn get-instances []
  (.list (.createProcessInstanceQuery (runtime-service))))

(defn convert-task [task]
  (let [name (.getName task)
        assignee (.getAssignee task)
        created (.getCreateTime task)
        due-date (.getDueDate task)
        id (.getId task)]
    {:name name
     :assignee assignee
     :created created
     :due-date due-date
     :id id}))

(defn get-task [id]
  (into {} (.getVariables (task-service) id)))

(defn get-tasks []
  (map convert-task (.list (.createTaskQuery (task-service)))))

(defn get-tasks-for-candidate-group [group]
  (map convert-task (.list (.taskCandidateGroup (.createTaskQuery (task-service)) group))))

(defn claim-task [id user]
  (.claim (task-service) id user))
