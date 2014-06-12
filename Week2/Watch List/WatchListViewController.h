//
//  WatchListViewController.h
//  Watch List
//
//  Created by Christopher Rockwell on 6/12/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>
#import "BZGFormField.h"

@interface WatchListViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
{
    IBOutlet UITableView *myTableView;
}

-(IBAction)onClick:(id)sender;
@end
