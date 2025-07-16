package main

import "net/http"

// func main() {

// }

type HttpClient struct {
	req    *http.Request
	client *http.Client
	method string
	url    string
	resp   *http.Response
}

func (h *HttpClient) Close() {
	h.resp.Body.Close()
}
