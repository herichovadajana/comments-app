(ns om-comment-box.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))


(def data {:comments [{:name "Twiggi"
                       :message "Wooof"}
                      {:name "Siggi"
                       :message "Ee ee ee ee"}
                      {:name "Ibi"
                       :message "Trrrrln"}]})

(defn title []
  (dom/div #js {:className "my-title"}
           (dom/span #js {:className "glyphicon glyphicon-pencil pencil"})
           (dom/h1 nil "Comment box")))

(defui CommentForm
  Object
  (render [this]
          (dom/div #js {}
                   (dom/div #js {:className "input-group my-input"}
                            (dom/span #js {:className "input-group-addon"
                                           :id "basic-addon"} "Name")
                            (dom/input #js {:type "text"
                                            :className "form-controll"
                                            :id "input-style"
                                            :placeholder ""}))
                   (dom/div #js {:className "input-group my-input"}
                            (dom/span #js {:className "input-group-addon"
                                           :id "basic-addon"} "Message")
                            (dom/input #js {:type "text"
                                            :className "form-controll"
                                            :id "input-style"
                                            :placeholder ""}))
                   (dom/button #js {:type "button"
                                    :className "btn btn-default btn-lg"}
                               "send"))))

(def commentform (om/factory CommentForm))

(defui Comment
  static om/IQuery
  (query [this]
         [:name :message])
  Object
  (render [this]
          (let [{:keys [name message]} (om/props this)]
            (dom/li #js {:className "one-comment"}
                    (dom/h4 #js {} name)
                    (dom/h3 #js {} " Message: ")
                    (dom/p #js {} message)))))

(def comment (om/factory Comment))

(defui RootView
  static om/IQuery
  (query [this]
         [:comments])
  Object
  (render [this]
          (let [{:keys [comments]} (om/props this)]
            (dom/div #js {:className "coll-md-12 need-margin"}
                     (title)
                     (commentform)
                     (dom/ul #js {:className "comments-list"}
                             (map comment comments))))))

(defn read [{:keys [state]} k _]
  {:value (get @state k)})

(def parser (om/parser {:read read}))

(def reconciler
  (om/reconciler
   {:state data
    :parser parser}))

(om/add-root! reconciler RootView (gdom/getElement "content"))
