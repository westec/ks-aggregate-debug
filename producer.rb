require "kafka"

kafka = Kafka.new(["localhost:9092"], client_id: "event-producer")


f = File.open("events.txt")
f.each_line { |l| 
  puts l
  kafka.deliver_message("#{l.strip}", topic: "ks-debug-input")
  sleep(3)
}


