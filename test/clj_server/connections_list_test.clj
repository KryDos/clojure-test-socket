(ns clj-server.connections_list-test
  (:require [clojure.test :refer :all]
            [clj-server.connections_list :as conn_l]))

(deftest add!-test
  (testing "Trying to add element to the agent"
    (conn_l/add! :devices "test string")
    (Thread/sleep 1000)
    (is (= (conn_l/count! :devices) 1))
    (conn_l/remove! :devices "test string")
    (Thread/sleep 1000)
    (is (= (conn_l/count! :devices) 0))))

(deftest remove!-test
  (testing "Remove element from agent"
    (conn_l/add! :devices "test string 2")
    (Thread/sleep 1000)
    (is (= (conn_l/count! :devices) 1))
    (conn_l/remove! :devices "test string 2")
    (Thread/sleep 1000)
    (is (= (conn_l/count! :devices) 0))))


