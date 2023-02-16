# Decorator
## Intent
## Problem
## Solution
Extending a class is the first thing that comes to your mind when you need to 
alter an object's behavior. However, inheritance has several serious caveats 
that you need to be aware of.

- Inheritance is static. You can't alter the behavior of an existing object at 
runtime. You can only replace the whole object with another one that's created
from a different subclass.

- Subclass can have just one parent class. In most languages,inheritance doesn't
let a class inherit behaviors of multiple classes as the same time.

One of the ways to overcome these caveats is by using Aggregation or Composition
instead of Inheritance. Both of the alternatives work almost the same way: one 
object has a reference to another and delegates it some work, whereas with 
inheritance, the object itself is able to do that work, inheriting the behavior
from its superclass.

With these new approach you can easily substitude the linked "helper" object 
with another, changing the behavior of the container at runtime. An object can 
use the behavior of various classes, having references to multiple objects and 
delegating them all kinds of work. Aggregation/composition is the key principle
behind many design patterns, including Decorator. On that note, let's return to
the pattern discussion.

"Wrapper" is the alternative nickname for the Decorator pattern that clearly 
expresses the main idea of the pattern. A wrapper is an object that can be 
linked with some *targer* object. The wrapper contains the same set of methods 
as the target and delegates to it all requests it receives. However,the wrapper
may alter the result by doing something either before or after it passes 
request to the target.

When does a simple wrapper become the real decorator? As I mentioned, the 
wrapper implements the same interface as the wrapped object. That's why from 
the client's perspective these objects are identical. Make the wrapper's 
reference field accept any object that follows that interface. This will let 
you cover an object with multiple wrappers,adding the combined hehavior of all
the wrappers to it.

In our notifications example, let's leave the simple email notification behavior
inside the base `Notifier` class, but trun all other notification methods into 
<!-- vim-markdown-toc Marked -->

<!-- vim-markdown-toc -->
decorators.

The client code would need to wrap a basic notifier object into a set of 
decorators that match the client's preferences. The resulting objects will be 
structured as a stack.

The last decorator in the stack would be the object that the client actully 
works with. Since all the decorators implement the same interface as the base 
notifier, the rest of the client code won't care wether it works with the 
'pure' notifier object or the decorated one.

Wiring clothes is an example of using decorator. When you're cold, you wrap 
yourself in a sweater. If you're still cold with a sweater, you can wear a 
jacket on top. If it's raining, you can put on a raincoat. All of these 
garments "extend" your basic behavior but aren't part of you, and you can 
easily take off any piece of clothing whenever you don't need it.



