et = ["t1", "t2", "t3", "t4", "t5", "t6", "t7"]
f = File.open("events.txt", "w")

(1..100).each { |id| 
  type_idx = rand(et.size)

  event = "#{id},#{et[type_idx]}"

  f.puts event

  puts event

}

f.close


