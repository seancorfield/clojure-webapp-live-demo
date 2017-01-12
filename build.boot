(def project 'webapp)
(def version "0.1.0-SNAPSHOT")

(set-env! :resource-paths #{"resources" "src"}
          :source-paths   #{"test"}
          :dependencies   '[[org.clojure/clojure "RELEASE"]
                            [ring "RELEASE"]
                            [proto-repl "RELEASE"]
                            [compojure "RELEASE"]
                            [com.stuartsierra/component "RELEASE"]
                            [seancorfield/boot-expectations "RELEASE" :scope "test"]])

(task-options!
 aot {:namespace   #{'webapp.core}}
 pom {:project     project
      :version     version
      :description "FIXME: write description"
      :url         "http://example/FIXME"
      :scm         {:url "https://github.com/yourname/webapp"}
      :license     {"Eclipse Public License"
                    "http://www.eclipse.org/legal/epl-v10.html"}}
 jar {:main        'webapp.core
      :file        (str "webapp-" version "-standalone.jar")})

(deftask build
  "Build the project locally as a JAR."
  [d dir PATH #{str} "the set of directories to write to (target)."]
  (let [dir (if (seq dir) dir #{"target"})]
    (comp (aot) (pom) (uber) (jar) (target :dir dir))))

(deftask run
  "Run the project."
  [a args ARG [str] "the arguments for the application."]
  (require '[webapp.core :as app])
  (apply (resolve 'app/-main) args))

(require '[seancorfield.boot-expectations :refer [expectations expecting]])
