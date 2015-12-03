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
  (initLocalState [this]
                  {:name "" :message ""})
  (render [this]
          (dom/form #js {:onSubmit (fn [event]
                                     (.preventDefault event)
                                     (.log js/console ((:submit-comment (om/props this))
                                                       {:name (:name (om/get-state this)) :message (:message (om/get-state this))})))}
                    (dom/div #js {:className "input-group my-input"}
                             (dom/span #js {:className "input-group-addon"
                                            :id "basic-addon"} "Name")
                             (dom/input #js {:type "text"
                                             :className "form-controll"
                                             :id "input-style"
                                             :value (:name (om/get-state this))
                                             :onChange (fn [event]
                                                         (om/update-state! this assoc :name (.. event -target -value)))
                                             :placeholder ""}))
                    (dom/div #js {:className "input-group my-input"}
                             (dom/span #js {:className "input-group-addon"
                                            :id "basic-addon"} "Message")
                             (dom/input #js {:type "text"
                                             :className "form-controll"
                                             :id "input-style"
                                             :placeholder ""
                                             :value (:message (om/get-state this))
                                             :onChange (fn [event]
                                                         (om/update-state! this assoc :message (.. event -target -value)))}))
                    (dom/button #js {:type "submit"
                                     :className "btn btn-default btn-lg"}
                                "send"))))

(def comment-form (om/factory CommentForm))

(defui CommentView
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

(def comment-view (om/factory CommentView))

(defui RootView
  static om/IQuery
  (query [this]
         [:comments])
  Object
  (render [this]
          (let [{:keys [comments]} (om/props this)]
            (dom/div #js {:className "coll-md-12 need-margin"}
                     (title)
                     (comment-form {:comments comments
                                    :submit-comment (fn [new-comment]
                                                      (om/transact! this `[(comment/update-input! {:new-comment ~new-comment})]))})
                     (dom/ul #js {:className "comments-list"}
                             (map comment-view comments))))))

(defmulti mutate (fn [_ k _] k))

(defmethod mutate 'comment/update-input!
  [{:keys [state]} _ {:keys [new-comment]}]
  {:action (fn [] (swap! state update-in [:comments] conj new-comment))})

(defn read [{:keys [state]} k _]
  {:value (get @state k)})

(def parser (om/parser {:read read :mutate mutate}))

(def reconciler
  (om/reconciler
   {:state data
    :parser parser}))

(om/add-root! reconciler RootView (gdom/getElement "content"))
